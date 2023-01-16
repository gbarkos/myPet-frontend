package com.example.mypet.activities
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentNewPetBinding
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.utils.getShortDate
import com.example.mypet.viewmodels.PetsViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker

class NewPetFragment : Fragment(R.layout.fragment_new_pet), ResponseFunctions {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentNewPetBinding
    lateinit var dialog : LoadingCircleDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewPetBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        dialog = LoadingCircleDialog()
        viewmodel.responseListener = this

        var calendarConstraint  = CalendarConstraints.Builder().setValidator(
            DateValidatorPointBackward.now()).build()

        // Date Picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(calendarConstraint)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener () {
            getShortDate(datePicker.selection)
            binding.newPetBirthdate.text = getShortDate(datePicker.selection).trim().toEditable()
        }

        // calendar button
        binding.newPetBirthdate.setOnClickListener(){
            datePicker.show(childFragmentManager , "a")
        }

        // Sex selection
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Αρσενικό", "Θηλυκό")
        )
        binding.newPetSex.setAdapter(adapter)

        var speciesAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Σκύλος", "Γάτα")
        )
        binding.newPetSpecies.setAdapter(speciesAdapter)

        // error listeners
        binding.newPetBirthdate.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newPetBirthdateHint.error = null
        }

        binding.newPetSex.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newPetSexHint.error = null
        }

        binding.newPetSpecies.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newPetSpecies.error = null
        }

        binding.newPetBreed.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newPetBreedHint.error = null
        }

        binding.newPetColor.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newPetColorHint.error = null
        }

        binding.newPetName.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newPetNameHint.error = null
        }

        // Submit button
        binding.submitNewPet.setOnClickListener(){
            var errorCounter = 0;

            if(binding.newPetName.text.isNullOrEmpty()){
                binding.newPetNameHint.error = "Παρακαλώ εισάγετε όνομα"
                errorCounter++
            }
            if(binding.newPetBirthdate.text.isNullOrEmpty()){
                binding.newPetBirthdateHint.error = "Παρακαλώ εισάγετε ημερομηνία γέννησης"
                errorCounter++
            }
            if(binding.newPetBreed.text.isNullOrEmpty()){
                binding.newPetBreedHint.error = "Παρακαλώ εισάγετε φυλή (ράτσα)"
                errorCounter++
            }
            if(binding.newPetColor.text.isNullOrEmpty()){
                binding.newPetColorHint.error = "Παρακαλώ εισάγετε χρώμα"
                errorCounter++
            }
            if(binding.newPetSex.text.isNullOrEmpty()){
                binding.newPetSexHint.error = "Παρακαλώ επιλέξτε φύλο"
                errorCounter++
            }
            if(binding.newPetSpecies.text.isNullOrEmpty()){
                binding.newPetSpeciesHint.error = "Παρακαλώ επιλέξτε είδος"
                errorCounter++
            }

            if(errorCounter == 0){
                var name = binding.newPetName.text.toString();
                var birthdate = binding.newPetBirthdate.text.toString();
                var sex = binding.newPetSex.text.toString();
                var breed = binding.newPetBreed.text.toString();
                var species = binding.newPetSpecies.text.toString()
                var id = binding.newPetID.text.toString()
                var color = binding.newPetColor.text.toString();
                var height = binding.newPetHeight.text.toString();
                var weight = binding.newPetWeight.text.toString();
                var marks = binding.newPetDistinguishingMarks.text.toString();


                viewmodel.addPet(id, name, birthdate, color, marks, breed, sex, weight, height, species)
            }
        }
    }

    override fun OnStarted() {
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        dialog.dismiss()
        binding.newPetLoadingBar.visibility = View.INVISIBLE
        findNavController().navigate(R.id.action_newPetFragment_to_petsFragment)
        Toast.makeText(context, "Επιτυχής προσθήκη κατοικίδιου", Toast.LENGTH_LONG).show()
    }

    override fun OnFailure(errorMsg: String?) {
        dialog.dismiss()
        Toast.makeText(context, "Αδυναμία προσθήκης κατοικίδιου", Toast.LENGTH_LONG).show()

    }
}

private fun String?.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}



