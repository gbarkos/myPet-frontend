package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentNewSurgeryBinding
import com.example.mypet.databinding.FragmentNewTreatmentBinding
import com.example.mypet.databinding.FragmentNewVermifugationBinding
import com.example.mypet.utils.EventObserver
import com.example.mypet.utils.NetworkLoadingState
import com.example.mypet.utils.getShortDate
import com.example.mypet.viewmodels.MedicalRecordViewModel
import com.google.android.material.datepicker.MaterialDatePicker

class NewSurgeryFragment : Fragment(R.layout.fragment_new_surgery) {

    private lateinit var viewmodel: MedicalRecordViewModel
    private lateinit var binding: FragmentNewSurgeryBinding
    private val surgeriesFragment = SurgeriesFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewSurgeryBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[MedicalRecordViewModel::class.java]

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

                viewmodel.addSurgery(medicalRecordId, name, date)
                viewmodel.getLoadStateFromRepo().observe(viewLifecycleOwner, EventObserver {
                    when (it) {
                        is NetworkLoadingState.OnLoading -> println("You can show loading indicator here or whatever to inform user that data is being loaded")
                        is NetworkLoadingState.OnSuccess -> {
                            val bundle = bundleOf("recordHasBeenUpdated" to true)
                            surgeriesFragment.arguments = bundle
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.navigationFragmentContainer, surgeriesFragment)
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