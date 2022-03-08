package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetSurgeryBinding
import com.example.mypet.databinding.FragmentPetTreatmentBinding
import com.example.mypet.databinding.FragmentPetVermifugationBinding
import com.example.mypet.models.Surgery
import com.example.mypet.models.Treatment
import com.example.mypet.models.Vermifugation
import com.example.mypet.utils.MongoDateAdapter
import com.example.mypet.viewmodels.PetsViewModel

class SurgeryInfoFragment  : Fragment(R.layout.fragment_pet_surgery){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetSurgeryBinding
    private lateinit var surgery: Surgery

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPetSurgeryBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsViewmodelSurgery= viewmodel
        val id = arguments?.getString("treatmentID")

        //val petID = viewmodel._id;
        //Toast.makeText(context, "$petID", Toast.LENGTH_LONG).show()

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pet!!!",it.pet.toString())
            for(item in it.pet.medicalRecord.surgeries){
                if(item._id.equals(id)){
                    surgery = item
                    break;
                }
            }
            populateViews(surgery)
        })
    }

    fun populateViews(it: Surgery){
        binding.apply{
            petSurgeryName.text = it.name.toEditable()
            petSurgeryDate.text = MongoDateAdapter(it.date).getDate().toEditable()
            petSurgeryVet.text =
                if(it.veterinarian != null) (it.veterinarian.surname+" "+it.veterinarian.name).toEditable()
                else "-".toEditable()
        }
    }
}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}