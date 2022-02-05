package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetInfoBinding
import com.example.mypet.databinding.FragmentPetVaccinationBinding
import com.example.mypet.models.Pet
import com.example.mypet.models.Vaccination
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.utils.MongoDateAdapter
import com.example.mypet.viewmodels.PetsViewModel

class VaccinationInfoFragment : Fragment(R.layout.fragment_pet_vaccination){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetVaccinationBinding
    private lateinit var vaccination: Vaccination

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPetVaccinationBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity()).get(PetsViewModel::class.java)
        binding.petsviewmodelVaccination = viewmodel
        val id = arguments?.getString("vaccinationID")

        //val petID = viewmodel._id;
        //Toast.makeText(context, "$petID", Toast.LENGTH_LONG).show()

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pet!!!",it.pet.toString())
            for(vaccine in it.pet.medicalRecord.vaccinations){
                if(vaccine._id.equals(id)){
                    vaccination = vaccine
                    break;
                }
            }
            populateViews(vaccination)
        })
    }

    fun populateViews(it: Vaccination){
        binding.apply{
            petVaccinationName.text = it.name.toEditable()
            petVaccinationExpirationDate.text =
                if(it.expirationDate != null) MongoDateAdapter(it.expirationDate).getDate().toEditable()
                else "-".toEditable()
            //petVaccinationManufacturer.text = it.manufacturer.toEditable()
            petVaccinationManufacturer.text = it.manufacturer.toEditable()
            petVaccinationBatchNumber.text = it.batchNumber?.toEditable()
            petVaccinationDate.text = MongoDateAdapter(it.vaccinationDate).getDate().toEditable()
            petVaccinationValidUntil.text = MongoDateAdapter(it.validUntil).getDate().toEditable()
            petVaccinationVeterinarian.text =
                if(it.veterinarian != null) (it.veterinarian?.surname+" "+it.veterinarian?.name).toEditable()
                else "-".toEditable()
        }
    }
}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}