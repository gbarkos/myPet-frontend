package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetVaccinationBinding
import com.example.mypet.databinding.FragmentPetVermifugationBinding
import com.example.mypet.models.Vaccination
import com.example.mypet.models.Vermifugation
import com.example.mypet.utils.MongoDateAdapter
import com.example.mypet.viewmodels.PetsViewModel

class VermifugationInfoFragment : Fragment(R.layout.fragment_pet_vermifugation){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetVermifugationBinding
    private lateinit var vermifugation: Vermifugation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPetVermifugationBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity()).get(PetsViewModel::class.java)
        binding.petsViewmodelVermifugation = viewmodel
        val id = arguments?.getString("vermifugationID")

        //val petID = viewmodel._id;
        //Toast.makeText(context, "$petID", Toast.LENGTH_LONG).show()

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pet!!!",it.pet.toString())
            for(item in it.pet.medicalRecord.vermifugations){
                if(item._id.equals(id)){
                    vermifugation = item
                    break;
                }
            }
            populateViews(vermifugation)
        })
    }

    fun populateViews(it: Vermifugation){
        binding.apply{
            petVermifugationName.text = it.name.toEditable()
            petVermifugationExpirationDate.text = MongoDateAdapter(it.expirationDate).getDate().toEditable()
            petVermifugationManufacturer.text = it.manufacturer.toEditable()
            petVermifugationDate.text = MongoDateAdapter(it.vermifugationDate).getDate().toEditable()
            petVermifugationValidUntil.text = MongoDateAdapter(it.validUntil).getDate().toEditable()
            petVermifugationVeterinarian.text = (it.veterinarian.surname+" "+it.veterinarian.name).toEditable()
        }
    }
}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}