package com.example.mypet.activities

import android.content.Intent
import android.os.Bundle
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
import com.example.mypet.databinding.FragmentPetsBinding
import com.example.mypet.models.Pet
import com.example.mypet.models.PetLimited
import com.example.mypet.models.responses.PetsLimitedGetResponse
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PetsFragment : Fragment(R.layout.fragment_pets) {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetsBinding
    private lateinit var petsList: List<PetLimited>
    private val newPetFragment = NewPetFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPetsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsviewmodel = viewmodel
        viewmodel.requestPets()
        viewmodel.getPetsDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pets!!!",it.pets.toString())
            petsList = it.pets

            binding.recyclerViewPets.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = PetsAdapter(petsList,lifecycleScope)
            }
            (binding.recyclerViewPets.adapter as PetsAdapter).onClick.onEach {
                val bundle = bundleOf("petID" to it._id)
                Log.d("OnItemClick",it._id)
                val intent = Intent (getActivity(), PetDetailsActivity::class.java)
                intent.putExtras(bundle)
                activity?.startActivity(intent)
            }.launchIn(lifecycleScope)
        })

        binding.addNewPetButton.setOnClickListener{
            findNavController().navigate(R.id.action_petsFragment_to_newPetFragment)
        }
    }
}