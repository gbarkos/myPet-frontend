package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetTreatmentBinding
import com.example.mypet.databinding.FragmentPetVermifugationBinding
import com.example.mypet.models.Treatment
import com.example.mypet.models.Vermifugation
import com.example.mypet.utils.MongoDateAdapter
import com.example.mypet.viewmodels.PetsViewModel

class TreatmentInfoFragment  : Fragment(R.layout.fragment_pet_treatment){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetTreatmentBinding
    private lateinit var treatment: Treatment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPetTreatmentBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsViewmodelTreatment = viewmodel
        val id = arguments?.getString("treatmentID")

        //val petID = viewmodel._id;
        //Toast.makeText(context, "$petID", Toast.LENGTH_LONG).show()

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pet!!!",it.pet.toString())
            for(item in it.pet.medicalRecord.treatments){
                if(item._id.equals(id)){
                    treatment = item
                    break;
                }
            }
            populateViews(treatment)
        })
    }

    fun populateViews(it: Treatment){
        binding.apply{
            petTreatmentDisease.text = it.disease.toEditable()
            petTreatmentStartDate.text = MongoDateAdapter(it.startOfTreatment).getDate().toEditable()
            petTreatmentEndDate.text = MongoDateAdapter(it.endOfTreatment).getDate().toEditable()
            petTreatmentMedicine.text = it.medicine.toEditable()
            petTreatmentFrequency.text = it.frequency.toString().toEditable()
            petTreatmentDuration.text = it.duration.toString().toEditable()
            petTreatmentVeterinarian.text =
                if(it.veterinarian != null) (it.veterinarian.surname+" "+it.veterinarian.name).toEditable()
                else "-".toEditable()
        }
    }
}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}