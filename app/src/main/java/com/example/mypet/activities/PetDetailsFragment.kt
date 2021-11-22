package com.example.mypet.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetDetailsBinding
import com.example.mypet.models.Pet
import com.example.mypet.models.Vaccination
import com.example.mypet.viewmodels.PetsViewModel

class PetDetailsFragment: Fragment(R.layout.fragment_pet_details) {

    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetDetailsBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val petID = arguments?.getString("petID")
        //Toast.makeText(context, "$petID", Toast.LENGTH_LONG).show()
        binding = FragmentPetDetailsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity()).get(PetsViewModel::class.java)
        binding.petsviewmodelDetails = viewmodel
        viewmodel._id = arguments?.getString("petID") //pass the id to the viewmodel, so that we can call requestPet(:id)

//      navController = findNavController()
//      binding.bottomNavigationView.setupWithNavController(navController)
    }
}