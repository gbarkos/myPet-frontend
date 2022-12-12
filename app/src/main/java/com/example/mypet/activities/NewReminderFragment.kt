package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentNewPetBinding
import com.example.mypet.databinding.FragmentNewReminderBinding
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.utils.getShortDate
import com.example.mypet.viewmodels.PetsViewModel
import com.example.mypet.viewmodels.RemindersViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import kotlin.collections.LinkedHashMap

class NewReminderFragment : Fragment(R.layout.fragment_new_reminder), ResponseFunctions {

    private lateinit var remindersViewmodel: RemindersViewModel
    private lateinit var petsViewmodel : PetsViewModel
    private lateinit var binding: FragmentNewReminderBinding
    lateinit var loadingDialog : LoadingCircleDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewReminderBinding.bind(view)
        remindersViewmodel = ViewModelProvider(requireActivity())[RemindersViewModel::class.java]
        petsViewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        remindersViewmodel.responseListener = this
        var petMap = mutableMapOf<String, String>()

        // Date picker
        var calendarConstraint  = CalendarConstraints.Builder().setValidator(
            DateValidatorPointBackward.now()).build()
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(calendarConstraint)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.addOnPositiveButtonClickListener () {

            binding.newReminderScheduledDate.text = getShortDate(datePicker.selection).trim().toEditable()
            Toast.makeText(context,  getShortDate(datePicker.selection), Toast.LENGTH_LONG).show()
        }
        binding.newReminderScheduledDate.setOnClickListener(){
            datePicker.show(childFragmentManager , "datepicker")
        }

        // Timepicker
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)+1
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(0)
                .build()
        timePicker.addOnPositiveButtonClickListener(){
            var value = timePicker.hour.toString() + ":" + timePicker.minute
            binding.newReminderScheduledTime.text = value.toEditable()
            Toast.makeText(context, value, Toast.LENGTH_LONG).show()
        }
        binding.newReminderScheduledTime.setOnClickListener {
            timePicker.show(childFragmentManager, "timepicker")
        }

        // Pets adapter
        petsViewmodel.getPetsDataFromRepo().observe(viewLifecycleOwner, {
            if(it != null){
                var petNames : MutableList<String> = mutableListOf()
                for(pet in it.pets){
                    petNames.add(pet.name)
                    petMap.put(pet.name, pet._id)
                }
                var petNamesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, petNames)
                binding.newReminderPet.setAdapter(petNamesAdapter)
            }
        })

        // Type adapter
        var typesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Εμβολιασμός","Αποπαρασίτωση","Θεραπεία"))
        binding.newReminderPet.setAdapter(typesAdapter)

        // Button listener
        binding.submitNewReminder.setOnClickListener {
            var errorCounter = 0;

            var name = binding.newReminderPet.text
            if(name.isNullOrEmpty()){
                binding.newReminderPetHint.error = "Το πεδίο είναι υποχρεωτικό"
                errorCounter++
            }
            var type = binding.newReminderType.text
            if(type.isNullOrEmpty()){
                binding.newReminderTypeHint.error = "Το πεδίο είναι υποχρεωτικό"
                errorCounter++
            }
            var date = binding.newReminderScheduledDate.text
            if(date.isNullOrEmpty()){
                binding.newReminderScheduledDateHint.error = "Το πεδίο είναι υποχρεωτικό"
                errorCounter++
            }
            var time = binding.newReminderScheduledTime.text
            if(time.isNullOrEmpty()){
                binding.newReminderScheduledTimeHint.error = "Το πεδίο είναι υποχρεωτικό"
                errorCounter++
            }

            if(errorCounter == 0){

            }
        }

    }

    override fun OnStarted() {
        TODO("Not yet implemented")
    }

    override fun OnSuccess() {
        TODO("Not yet implemented")
    }

    override fun OnFailure(errorMsg: String?) {
        TODO("Not yet implemented")
    }

    private fun String?.toEditable(): Editable? {
        return Editable.Factory.getInstance().newEditable(this)
    }

}