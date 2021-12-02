package com.example.mypet.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypet.R
import com.example.mypet.adapters.PetsAdapter
import com.example.mypet.databinding.FragmentPetInfoBinding
import com.example.mypet.models.Pet
import com.example.mypet.utils.MongoDateAdapter
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PetInfoFragment: Fragment(R.layout.fragment_pet_info) {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetInfoBinding
    private lateinit var pet: Pet
    //private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        //val petID = arguments?.getString("petID")


        binding = FragmentPetInfoBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity()).get(PetsViewModel::class.java)
        binding.petsviewmodelInfo = viewmodel

        //val petID = viewmodel._id;
        //Toast.makeText(context, "$petID", Toast.LENGTH_LONG).show()
        viewmodel.requestPet(viewmodel._id.toString())
        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pet!!!",it.pet.toString())
            populateViews(it);
        })

        //navController = findNavController()
        //binding.bottomNavigationView.setupWithNavController(navController)
    }

    fun populateViews(it: PetGetResponse){
        binding.apply{
            petInfoName.text = it.pet.name.toEditable();
            petInfoBirthdate.text = MongoDateAdapter(it.pet.birthdate).getDate().toEditable()
            petInfoSex.text = it.pet.sex.toEditable()
            petInfoBreed.text = it.pet.breed.toEditable()
            petInfoID.text = it.pet.id.toEditable()
            petInfoColor.text = it.pet.colour.toEditable()
            petInfoHeight.text = (it.pet.height.toString()+" m").toEditable()
            petInfoWeight.text = (it.pet.weight.toString()+" Kg").toEditable()
            petInfoDistinguishingMarks.text = it.pet.distinguishingMarks.toEditable()
        }
    }
}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}


