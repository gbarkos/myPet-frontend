package com.example.mypet.activities

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentNewVermifugationBinding
import com.example.mypet.models.Vet
import com.example.mypet.utils.*
import com.example.mypet.viewmodels.MedicalRecordViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson

class NewVermifugationFragment : Fragment(R.layout.fragment_new_vermifugation), ResponseFunctions {

    private lateinit var viewmodel: MedicalRecordViewModel
    private lateinit var binding: FragmentNewVermifugationBinding
    private val vermifugationsFragment = VermifugationsFragment()
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var dialog : LoadingCircleDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentNewVermifugationBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[MedicalRecordViewModel::class.java]
        viewmodel.responseListener = this
        adjustViewForVet()
        dialog = LoadingCircleDialog()

        // Vermifugation Date Picker
        val vermifugationDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        vermifugationDatePicker.addOnPositiveButtonClickListener () {
            getShortDate(vermifugationDatePicker.selection)
            binding.newVermifugationVermifugationDate.text = getShortDate(vermifugationDatePicker.selection).trim().toEditable()
        }

        binding.newVermifugationVermifugationDate.setOnClickListener(){
            vermifugationDatePicker.show(childFragmentManager , "a")
        }

        //Valid Until Picker
        val validUntilPicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        validUntilPicker.addOnPositiveButtonClickListener () {
            getShortDate(validUntilPicker.selection)
            binding.newVermifugationValidUntil.text = getShortDate(validUntilPicker.selection).trim().toEditable()
        }

        binding.newVermifugationValidUntil.setOnClickListener(){
            validUntilPicker.show(childFragmentManager , "a")
        }

        //Expiration Picker
        val expirationDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        expirationDatePicker.addOnPositiveButtonClickListener () {
            getShortDate(expirationDatePicker.selection)
            binding.newVermifugationExpirationDate.text = getShortDate(expirationDatePicker.selection).trim().toEditable()
        }

        binding.newVermifugationExpirationDate.setOnClickListener(){
            expirationDatePicker.show(childFragmentManager , "a")
        }

        // Error listeners
        binding.newVermifugationName.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newVermifugationNameHint.error = null
        }

        binding.newVermifugationManufacturer.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newVermifugationManufacturerHint.error = null
        }

        binding.newVermifugationVermifugationDate.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newVermifugationVermifugationDateHint.error = null
        }

        binding.newVermifugationValidUntil.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newVermifugationValidUntilHint.error = null
        }

        // Submit button
        binding.newVaccinationSubmit.setOnClickListener(){
            var errorCounter = 0;

            if(binding.newVermifugationManufacturer.text.isNullOrEmpty()){
                binding.newVermifugationManufacturerHint.error = "Παρακαλώ εισάγετε όνομα κατασκευαστή"
                errorCounter++
            }
            if(binding.newVermifugationName.text.isNullOrEmpty()){
                binding.newVermifugationNameHint.error = "Παρακαλώ εισάγετε ονομασία θεραπείας"
                errorCounter++
            }
            if(binding.newVermifugationVermifugationDate.text.isNullOrEmpty()){
                binding.newVermifugationVermifugationDateHint.error = "Παρακαλώ εισάγετε ημερομηνία χορήγησης θεραπείας"
                errorCounter++
            }
            if(binding.newVermifugationValidUntil.text.isNullOrEmpty()){
                binding.newVermifugationValidUntilHint.error = "Παρακαλώ εισάγετε ημερομηνία λήξης ισχύος"
                errorCounter++
            }

            if(errorCounter == 0){
                var name = binding.newVermifugationName.text.toString();
                var manufacturer = binding.newVermifugationManufacturer.text.toString();
                var vermifugationDate = binding.newVermifugationVermifugationDate.text.toString();
                var validUntil = binding.newVermifugationValidUntil.text.toString();
                var expirationDate = binding.newVermifugationExpirationDate.text.toString();
                var medicalRecordId = arguments?.getString("recordId")

                var stringVet = SharedPreferencesUtil.getVetData()
                if(!stringVet.isNullOrEmpty()) {
                    var gson = Gson()
                    var vet = gson.fromJson(stringVet, Vet::class.java)
                    viewmodel.addVermifugation(medicalRecordId, manufacturer, name, expirationDate,
                        vermifugationDate, validUntil, vet)

                }else{
                    viewmodel.addVermifugation(medicalRecordId, manufacturer, name, expirationDate,
                        vermifugationDate, validUntil)
                }
            }
        }
    }

    private fun adjustViewForVet(){
        var stringVet = SharedPreferencesUtil.getVetData()
        if(!stringVet.isNullOrEmpty()) {
            var gson = Gson()
            var vet = gson.fromJson(stringVet, Vet::class.java)

            binding.apply {
                topAppBar.setBackgroundColor(resources.getColor(R.color.vet_blue))
                backgroundLayout.background = resources.getDrawable(R.drawable.generic_background_vet)
                newVaccinationSubmit.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }

    override fun OnStarted() {
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        dialog.dismiss()
        val bundle = bundleOf("recordHasBeenUpdated" to true)
        vermifugationsFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.navigationFragmentContainer, vermifugationsFragment)
        transaction?.disallowAddToBackStack()
        transaction?.commit()
    }

    override fun OnFailure(errorMsg: String?) {
        dialog.dismiss()
        Toast.makeText(context, "Αδυναμία προσθήκης θεραπείας", Toast.LENGTH_LONG).show()
    }
}

private fun String?.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}