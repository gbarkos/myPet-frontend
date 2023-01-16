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
import com.example.mypet.databinding.FragmentNewDiagnosticTestBinding
import com.example.mypet.models.Vet
import com.example.mypet.utils.*
import com.example.mypet.viewmodels.MedicalRecordViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson

class NewDiagnosticTestFragment : Fragment(R.layout.fragment_new_diagnostic_test), ResponseFunctions {

    private lateinit var viewmodel: MedicalRecordViewModel
    private lateinit var binding: FragmentNewDiagnosticTestBinding
    private val diagnosticTestsFragment = DiagnosticTestsFragment()
    lateinit var dialog : LoadingCircleDialog
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentNewDiagnosticTestBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[MedicalRecordViewModel::class.java]
        viewmodel.responseListener = this
        dialog = LoadingCircleDialog()
        adjustViewForVet()

        var calendarConstraint  = CalendarConstraints.Builder().setValidator(
            DateValidatorPointBackward.now()).build()

        //Surgery Date Picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(calendarConstraint)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener () {
            getShortDate(datePicker.selection)
            binding.newDiagnosticTestDate.text = getShortDate(datePicker.selection).trim().toEditable()
        }

        binding.newDiagnosticTestDate.setOnClickListener(){ //TODO
            datePicker.show(childFragmentManager , "a")
        }


        // Error listeners
        binding.newDiagnosticTestName.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newDiagnosticTestNameHint.error = null
        }

        binding.newDiagnosticTestResult.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newDiagnosticTestResultHint.error = null
        }

        binding.newDiagnosticTestDate.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newDiagnosticTestDateHint.error = null
        }


        // Submit button
        binding.newDiagnosticTestSubmit.setOnClickListener(){
            var errorCounter = 0;

            if(binding.newDiagnosticTestName.text.isNullOrEmpty()){
                binding.newDiagnosticTestNameHint.error = "Παρακαλώ εισάγετε όνομα ελέγχου"
                errorCounter++
            }
            if(binding.newDiagnosticTestDate.text.isNullOrEmpty()){
                binding.newDiagnosticTestDateHint.error = "Παρακαλώ εισάγετε ημερομηνία ελεχου"
                errorCounter++
            }
            if(binding.newDiagnosticTestResult.text.isNullOrEmpty()){
                binding.newDiagnosticTestResultHint.error = "Παρακαλώ εισάγετε αποτέλεσμα ελέγχου"
                errorCounter++
            }

            if(errorCounter == 0){
                var name = binding.newDiagnosticTestName.text.toString()
                var result = binding.newDiagnosticTestResult.text.toString()
                var date = binding.newDiagnosticTestDate.text.toString()
                var medicalRecordId = arguments?.getString("recordId")

                var stringVet = SharedPreferencesUtil.getVetData()
                if(!stringVet.isNullOrEmpty()) {
                    var gson = Gson()
                    var vet = gson.fromJson(stringVet, Vet::class.java)
                    viewmodel.addDiagnosticTest(medicalRecordId, name, date, result, vet)
                }else{
                    viewmodel.addDiagnosticTest(medicalRecordId, name, date, result)
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
                newDiagnosticTestSubmit.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }

    override fun OnStarted() {
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        dialog.dismiss()
        val bundle = bundleOf("recordHasBeenUpdated" to true)
        diagnosticTestsFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.navigationFragmentContainer, diagnosticTestsFragment)
        transaction?.disallowAddToBackStack()
        transaction?.commit()
    }

    override fun OnFailure(errorMsg: String?) {
        dialog.dismiss()
        Toast.makeText(context, "Αδυναμία προσθήκης διαγνωστικού τεστ", Toast.LENGTH_LONG).show()
    }
}

private fun String?.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}