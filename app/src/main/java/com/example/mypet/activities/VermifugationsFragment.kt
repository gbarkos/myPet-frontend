package com.example.mypet.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypet.R
import com.example.mypet.adapters.VaccinationsAdapter
import com.example.mypet.adapters.VermifugationsAdapter
import com.example.mypet.databinding.FragmentVaccinationsBinding
import com.example.mypet.databinding.FragmentVermifugationsBinding
import com.example.mypet.models.Vaccination
import com.example.mypet.models.Vermifugation
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class VermifugationsFragment: Fragment(R.layout.fragment_vermifugations){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentVermifugationsBinding
    private val vermifugationInfoFragment = VermifugationInfoFragment()

    private lateinit var vermifugationsList: List<Vermifugation>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVermifugationsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
       // binding.petsviewmodel = viewmodel

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pets!!!",it.pet.toString())
            vermifugationsList = it.pet.medicalRecord.vermifugations
            binding.recyclerViewVermifugations.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = VermifugationsAdapter(vermifugationsList,lifecycleScope)
            }

            (binding.recyclerViewVermifugations.adapter as VermifugationsAdapter).onClick.onEach {
                Log.d("OnItemClick",it._id)
                vermifugationInfoFragment.arguments = bundleOf("vermifugationID" to it._id)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(this.id, vermifugationInfoFragment)
                //transaction.addToBackStack("null")
                transaction.commit()

            }.launchIn(lifecycleScope)
        })
    }
}