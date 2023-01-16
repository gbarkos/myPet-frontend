package com.example.mypet.activities

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetVaccinationBinding
import com.example.mypet.models.Vaccination
import com.example.mypet.utils.MongoDateAdapter
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.PetsViewModel

class VaccinationInfoFragment : Fragment(R.layout.fragment_pet_vaccination){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetVaccinationBinding
    private lateinit var vaccination: Vaccination
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentPetVaccinationBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity()).get(PetsViewModel::class.java)
        binding.petsviewmodelVaccination = viewmodel
        adjustViewForVet()
        val id = arguments?.getString("vaccinationID")

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

            petVaccinationManufacturer.text = it.manufacturer.toEditable()
            petVaccinationBatchNumber.text =
                if(it.batchNumber!=null && it.batchNumber!="") it.batchNumber.toEditable()
                else "-".toEditable();
            petVaccinationDate.text = MongoDateAdapter(it.vaccinationDate).getDate().toEditable()
            petVaccinationValidUntil.text = MongoDateAdapter(it.validUntil).getDate().toEditable()
            petVaccinationVeterinarian.text =
                if(it.veterinarian != null) (it.veterinarian?.surname+" "+it.veterinarian?.name).toEditable()
                else "-".toEditable()
        }
    }

    private fun adjustViewForVet(){
        var stringVet = SharedPreferencesUtil.getVetData()
        if(!stringVet.isNullOrEmpty()) {
            binding.apply {
                topAppBar.setBackgroundColor(resources.getColor(R.color.vet_blue))
                backgroundLayout.background = resources.getDrawable(R.drawable.generic_background_vet)
            }
        }
    }
}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}