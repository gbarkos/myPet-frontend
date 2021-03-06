package com.example.mypet.activities
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentNewPetBinding
import com.example.mypet.utils.EventObserver
import com.example.mypet.utils.NetworkLoadingState
import com.example.mypet.utils.getShortDate
import com.example.mypet.viewmodels.PetsViewModel
import com.google.android.material.datepicker.MaterialDatePicker

class NewPetFragment : Fragment(R.layout.fragment_new_pet) {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentNewPetBinding
    lateinit var dialog : LoadingDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewPetBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        //binding.petsviewmodel = viewmodel
        dialog = LoadingDialogFragment(activity)

        //Date Picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
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

        //Sex selection
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Αρσενικό", "Θηλυκό")
        )
        binding.newPetSex.setAdapter(adapter)
        // error listeners
        binding.newPetBirthdate.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newPetBirthdateHint.error = null
        }

        binding.newPetSex.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty()) binding.newPetSexHint.error = null
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

            if(errorCounter == 0){
                var name = binding.newPetName.text.toString();
                var birthdate = binding.newPetBirthdate.text.toString();
                var sex = binding.newPetSex.text.toString();
                var breed = binding.newPetBreed.text.toString();

                var id = binding.newPetID.text.toString()
                var color = binding.newPetColor.text.toString();
                var height = binding.newPetHeight.text.toString();
                var weight = binding.newPetWeight.text.toString();
                var marks = binding.newPetDistinguishingMarks.text.toString();


                viewmodel.addPet(id, name, birthdate, color, marks, breed, sex, weight, height)
                viewmodel.getLoadStateFromRepo().observe(viewLifecycleOwner, EventObserver {
                    when (it) {
                        is NetworkLoadingState.OnLoading -> {
                            println("Loading...")
                            binding.newPetLoadingBar.visibility = View.VISIBLE

                        }
                        is NetworkLoadingState.OnSuccess -> {
                            binding.newPetLoadingBar.visibility = View.INVISIBLE
                            findNavController().navigate(R.id.action_newPetFragment_to_petsFragment)
                        }
                        is NetworkLoadingState.OnError -> {
                            binding.newPetLoadingBar.visibility = View.INVISIBLE
                            println(it.message)
                        }
                    }})
            }
        }
    }
}



private fun String?.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}



