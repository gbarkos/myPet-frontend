package com.example.mypet.activities

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PetsFragment : Fragment(R.layout.fragment_pets) {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetsBinding
    private lateinit var petsList: List<Pet>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPetsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity()).get(PetsViewModel::class.java)

        viewmodel.requestPets()
        viewmodel.getPetsDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pets!!!",it.pets.toString())
            petsList = it.pets;
            initRecyclerView()
            val adapter = PetsAdapter(petsList, lifecycleScope)
            adapter.onClick.onEach {
                val bundle = bundleOf("petID" to it._id)
                //findNavController().navigate(R.id......., bundle) //TODO
            }.launchIn(lifecycleScope)
        });
    }

    private fun initRecyclerView(){
        binding.recyclerViewPets.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PetsAdapter(petsList,lifecycleScope)
        }
    }
}