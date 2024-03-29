package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentNewDiagnosticTestBinding
import com.example.mypet.databinding.FragmentNewSurgeryBinding
import com.example.mypet.databinding.FragmentNewTreatmentBinding
import com.example.mypet.databinding.FragmentNewVermifugationBinding
import com.example.mypet.utils.EventObserver
import com.example.mypet.utils.NetworkLoadingState
import com.example.mypet.utils.getShortDate
import com.example.mypet.viewmodels.MedicalRecordViewModel
import com.google.android.material.datepicker.MaterialDatePicker

class NewDiagnosticTestFragment : Fragment(R.layout.fragment_new_diagnostic_test) {

    private lateinit var viewmodel: MedicalRecordViewModel
    private lateinit var binding: FragmentNewDiagnosticTestBinding
    private val diagnosticTestsFragment = DiagnosticTestsFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewDiagnosticTestBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[MedicalRecordViewModel::class.java]

        //Surgery Date Picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
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

                viewmodel.addDiagnosticTest(medicalRecordId, name, date, result)
                viewmodel.getLoadStateFromRepo().observe(viewLifecycleOwner, EventObserver {
                    when (it) {
                        is NetworkLoadingState.OnLoading -> println("You can show loading indicator here or whatever to inform user that data is being loaded")
                        is NetworkLoadingState.OnSuccess -> {
                            val bundle = bundleOf("recordHasBeenUpdated" to true)
                            diagnosticTestsFragment.arguments = bundle
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.navigationFragmentContainer, diagnosticTestsFragment)
                            transaction?.disallowAddToBackStack()
                            transaction?.commit()
                        }
                        is NetworkLoadingState.OnError -> println(it.message)
                    }})
            }
        }
    }
}

private fun String?.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}