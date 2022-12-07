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
import com.example.mypet.databinding.FragmentNewTreatmentBinding
import com.example.mypet.databinding.FragmentNewVermifugationBinding
import com.example.mypet.utils.*
import com.example.mypet.viewmodels.MedicalRecordViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.material.datepicker.MaterialDatePicker

class NewTreatmentFragment : Fragment(R.layout.fragment_new_treatment), AuthFunctions { //TODO

    private lateinit var viewmodel: MedicalRecordViewModel
    private lateinit var binding: FragmentNewTreatmentBinding //TODO
    private val treatmentsFragment = TreatmentsFragment()
    lateinit var dialog : testDialog
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentNewTreatmentBinding.bind(view) //TODO
        viewmodel = ViewModelProvider(requireActivity())[MedicalRecordViewModel::class.java]
        viewmodel.authListener = this
        dialog = testDialog()
        adjustViewForVet()

        //Vermifugation Date Picker
        val startDateDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        startDateDatePicker.addOnPositiveButtonClickListener () {
            getShortDate(startDateDatePicker.selection)
            binding.newTreatmentStartDate.text = getShortDate(startDateDatePicker.selection).trim().toEditable() //TODO
        }

        binding.newTreatmentStartDate.setOnClickListener(){ //TODO
            startDateDatePicker.show(childFragmentManager , "a")
        }

        //Valid Until Picker
        val endDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        endDatePicker.addOnPositiveButtonClickListener () {
            getShortDate(endDatePicker.selection)
            binding.newTreatmentEndDate.text = getShortDate(endDatePicker.selection).trim().toEditable() //TODO
        }

        binding.newTreatmentEndDate.setOnClickListener(){ //TODO
            endDatePicker.show(childFragmentManager , "a")
        }


        // Error listeners
        binding.newTreatmentDisease.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newTreatmentDiseaseHint.error = null
        }

        binding.newTreatmentMedicine.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newTreatmentMedicine.error = null
        }

        binding.newTreatmentStartDate.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newTreatmentStartDateHint.error = null
        }

        binding.newTreatmentEndDate.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newTreatmentEndDate.error = null
        }

        binding.newTreatmentFrequency.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newTreatmentFrequencyHint.error = null
        }

        // Submit button
        binding.newTreatmentSubmit.setOnClickListener(){
            var errorCounter = 0;

            if(binding.newTreatmentDisease.text.isNullOrEmpty()){
                binding.newTreatmentDiseaseHint.error = "Παρακαλώ εισάγετε όνομα ασθένειας"
                errorCounter++
            }
            if(binding.newTreatmentMedicine.text.isNullOrEmpty()){
                binding.newTreatmentMedicineHint.error = "Παρακαλώ εισάγετε όνομα φαρμάκου"
                errorCounter++
            }
            if(binding.newTreatmentFrequency.text.isNullOrEmpty()){
                binding.newTreatmentFrequencyHint.error = "Παρακαλώ εισάγετε συχνότητα χορήγησης"
                errorCounter++
            }
            if(binding.newTreatmentStartDate.text.isNullOrEmpty()){
                binding.newTreatmentStartDateHint.error = "Παρακαλώ εισάγετε ημερομηνία έναρξης θεραπείας"
                errorCounter++
            }
            if(binding.newTreatmentEndDate.text.isNullOrEmpty()){
                binding.newTreatmentEndDateHint.error = "Παρακαλώ εισάγετε ημερομηνία λήξης θεραπείας"
                errorCounter++
            }

            if(errorCounter == 0){
                var disease = binding.newTreatmentDisease.text.toString()
                var medicine = binding.newTreatmentMedicine.text.toString()
                var startDate = binding.newTreatmentStartDate.text.toString()
                var endDate = binding.newTreatmentEndDate.text.toString()
                var frequency = binding.newTreatmentFrequency.text.toString()
                var medicalRecordId = arguments?.getString("recordId")

                viewmodel.addTreatment(medicalRecordId, medicine, disease, startDate, endDate, frequency)
            }
        }
    }

    private fun adjustViewForVet(){
        var stringVet = SharedPreferencesUtil.getVetData()
        if(!stringVet.isNullOrEmpty()) {
            binding.apply {
                topAppBar.setBackgroundColor(resources.getColor(R.color.vet_blue))
                backgroundLayout.background = resources.getDrawable(R.drawable.generic_background_vet)
                newTreatmentSubmit.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }

    override fun OnStarted() {
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        dialog.dismiss()
        val bundle = bundleOf("recordHasBeenUpdated" to true)
        treatmentsFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.navigationFragmentContainer, treatmentsFragment)
        transaction?.disallowAddToBackStack()
        transaction?.commit()
    }

    override fun OnFailure(errorCode: MutableList<Int>?) {
        dialog.dismiss()
        Toast.makeText(context, "Αδυναμία προσθήκης θεραπείας", Toast.LENGTH_LONG).show()
    }
}

private fun String?.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}