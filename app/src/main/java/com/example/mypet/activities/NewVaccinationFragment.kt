package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentNewPetBinding
import com.example.mypet.databinding.FragmentNewVaccinationBinding
import com.example.mypet.utils.EventObserver
import com.example.mypet.utils.NetworkLoadingState
import com.example.mypet.utils.getShortDate
import com.example.mypet.viewmodels.MedicalRecordViewModel
import com.example.mypet.viewmodels.PetsViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class NewVaccinationFragment : Fragment(R.layout.fragment_new_vaccination) {

    private lateinit var viewmodel: MedicalRecordViewModel
    private lateinit var binding: FragmentNewVaccinationBinding
    private val vaccinationsFragment = VaccinationsFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewVaccinationBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[MedicalRecordViewModel::class.java]

        //Vaccination Date Picker
        val vaccinationDatePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        vaccinationDatePicker.addOnPositiveButtonClickListener () {
            getShortDate(vaccinationDatePicker.selection)
            binding.newVaccinationVaccinationDate.text = getShortDate(vaccinationDatePicker.selection).trim().toEditable()
        }

        binding.newVaccinationVaccinationDate.setOnClickListener(){
            vaccinationDatePicker.show(childFragmentManager , "a")
        }

        //Valid Until Picker
        val validUntilPicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        validUntilPicker.addOnPositiveButtonClickListener () {
            getShortDate(validUntilPicker.selection)
            binding.newVaccinationValidUntil.text = getShortDate(validUntilPicker.selection).trim().toEditable()
        }

        binding.newVaccinationValidUntil.setOnClickListener(){
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
            binding.newVaccinationExpirationDate.text = getShortDate(expirationDatePicker.selection).trim().toEditable()
        }

        binding.newVaccinationExpirationDate.setOnClickListener(){
            expirationDatePicker.show(childFragmentManager , "a")
        }


        // Error listeners
        binding.newVaccinationManufacturer.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newVaccinationManufacturerHint.error = null
        }

        binding.newVaccinationName.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newVaccinationNameHint.error = null
        }

        binding.newVaccinationVaccinationDate.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newVaccinationVaccinationDateHint.error = null
        }

        binding.newVaccinationValidUntil.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newVaccinationValidUntilHint.error = null
        }


        // Submit button
        binding.newVaccinationSubmit.setOnClickListener(){
            var errorCounter = 0;

            if(binding.newVaccinationManufacturer.text.isNullOrEmpty()){
                binding.newVaccinationManufacturerHint.error = "Παρακαλώ εισάγετε όνομα κατασκευαστή"
                errorCounter++
            }
            if(binding.newVaccinationName.text.isNullOrEmpty()){
                binding.newVaccinationNameHint.error = "Παρακαλώ εισάγετε ονομασία εμβολίου"
                errorCounter++
            }
            if(binding.newVaccinationVaccinationDate.text.isNullOrEmpty()){
                binding.newVaccinationVaccinationDateHint.error = "Παρακαλώ εισάγετε ημερομηνία εμβολιασμού"
                errorCounter++
            }
            if(binding.newVaccinationValidUntil.text.isNullOrEmpty()){
                binding.newVaccinationValidUntilHint.error = "Παρακαλώ εισάγετε ημερομηνία λήξης ισχύος"
                errorCounter++
            }

            if(errorCounter == 0){
                var name = binding.newVaccinationName.text.toString();
                var manufacturer = binding.newVaccinationManufacturer.text.toString();
                var batchNumber = binding.newVaccinationBatchNumber.text.toString();
                var vaccinationDate = binding.newVaccinationVaccinationDate.text.toString();
                var validUntil = binding.newVaccinationValidUntil.text.toString();
                var expirationDate = binding.newVaccinationExpirationDate.text.toString();
                var medicalRecordId = arguments?.getString("recordId")

                viewmodel.addVaccination(medicalRecordId, batchNumber, manufacturer, name, expirationDate, vaccinationDate, validUntil)
                viewmodel.getLoadStateFromRepo().observe(viewLifecycleOwner, EventObserver {
                    when (it) {
                        is NetworkLoadingState.OnLoading -> println("You can show loading indicator here or whatever to inform user that data is being loaded")
                        is NetworkLoadingState.OnSuccess -> {
                            val bundle = bundleOf("recordHasBeenUpdated" to true)
                            vaccinationsFragment.arguments = bundle
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.navigationFragmentContainer, vaccinationsFragment)
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
