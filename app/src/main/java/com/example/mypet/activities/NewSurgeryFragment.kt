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
import com.example.mypet.databinding.FragmentNewSurgeryBinding
import com.example.mypet.databinding.FragmentNewTreatmentBinding
import com.example.mypet.databinding.FragmentNewVermifugationBinding
import com.example.mypet.models.Vet
import com.example.mypet.utils.*
import com.example.mypet.viewmodels.MedicalRecordViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson

class NewSurgeryFragment : Fragment(R.layout.fragment_new_surgery), AuthFunctions {

    private lateinit var viewmodel: MedicalRecordViewModel
    private lateinit var binding: FragmentNewSurgeryBinding
    private val surgeriesFragment = SurgeriesFragment()
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var dialog : testDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentNewSurgeryBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[MedicalRecordViewModel::class.java]
        viewmodel.authListener = this
        dialog = testDialog()
        adjustViewForVet()
        //Surgery Date Picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener () {
            getShortDate(datePicker.selection)
            binding.newSurgeryDate.text = getShortDate(datePicker.selection).trim().toEditable()
        }

        binding.newSurgeryDate.setOnClickListener(){ //TODO
            datePicker.show(childFragmentManager , "a")
        }


        // Error listeners
        binding.newSurgeryName.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newSurgeryNameHint.error = null
        }

        binding.newSurgeryDate.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newSurgeryDateHint.error = null
        }


        // Submit button
        binding.newSurgerySubmit.setOnClickListener(){
            var errorCounter = 0;

            if(binding.newSurgeryDate.text.isNullOrEmpty()){
                binding.newSurgeryDateHint.error = "Παρακαλώ εισάγετε ημερομηνία εγχείρισης"
                errorCounter++
            }
            if(binding.newSurgeryName.text.isNullOrEmpty()){
                binding.newSurgeryNameHint.error = "Παρακαλώ εισάγετε όνομα εγχείρισης"
                errorCounter++
            }

            if(errorCounter == 0){
                var name = binding.newSurgeryName.text.toString()
                var date = binding.newSurgeryDate.text.toString()
                var medicalRecordId = arguments?.getString("recordId")

                var stringVet = SharedPreferencesUtil.getVetData()
                if(!stringVet.isNullOrEmpty()) {
                    var gson = Gson()
                    var vet = gson.fromJson(stringVet, Vet::class.java)
                    viewmodel.addSurgery(medicalRecordId, name, date, vet)
                }else{
                    viewmodel.addSurgery(medicalRecordId, name, date)
                }
            }
        }
    }

    private fun adjustViewForVet(){
        var stringVet = SharedPreferencesUtil.getVetData()
        if(!stringVet.isNullOrEmpty()) {
            binding.apply {
                topAppBar.setBackgroundColor(resources.getColor(R.color.vet_blue))
                backgroundLayout.background = resources.getDrawable(R.drawable.generic_background_vet)
                newSurgerySubmit.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }

    override fun OnStarted() {
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        dialog.dismiss()
        val bundle = bundleOf("recordHasBeenUpdated" to true)
        surgeriesFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.navigationFragmentContainer, surgeriesFragment)
        transaction?.disallowAddToBackStack()
        transaction?.commit()
    }

    override fun OnFailure(errorCode: MutableList<Int>?) {
        dialog.dismiss()
        Toast.makeText(context, "Αδυναμία προσθήκης εγχείρισης", Toast.LENGTH_LONG).show()
    }
}

private fun String?.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}