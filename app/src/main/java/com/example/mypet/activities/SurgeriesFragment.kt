package com.example.mypet.activities

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypet.R
import com.example.mypet.adapters.SurgeriesAdapter
import com.example.mypet.databinding.FragmentSurgeriesBinding
import com.example.mypet.models.Surgery
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SurgeriesFragment : Fragment(R.layout.fragment_surgeries){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentSurgeriesBinding
    private val surgeryInfoFragment = SurgeryInfoFragment()
    private lateinit var newSurgeryFragment : NewSurgeryFragment
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var surgeriesList: List<Surgery>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentSurgeriesBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsViewmodelSurgeries = viewmodel
        adjustViewForVet()

        val petID = viewmodel._id
        val recordId = viewmodel.recordId
        val receivedBundle = arguments?.getBoolean("recordHasBeenUpdated")
        if(receivedBundle == true) { // if there's an update on the medical record, re-fetch the data
            if(petID != null){
                viewmodel.requestPet(petID)
            }
        }

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner) {
            Log.d("Pets!!!", it.pet.toString())
            surgeriesList = it.pet.medicalRecord.surgeries
            binding.recyclerViewSurgeries.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = SurgeriesAdapter(surgeriesList, lifecycleScope)
            }

            (binding.recyclerViewSurgeries.adapter as SurgeriesAdapter).onClick.onEach {
                Log.d("OnItemClick", it._id)
                surgeryInfoFragment.arguments = bundleOf("treatmentID" to it._id)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(this.id, surgeryInfoFragment)
                transaction.commit()

            }.launchIn(lifecycleScope)
        }

        // send the pet id so that the addVaccination request knows what to update
        binding.addNewSurgeryButton.setOnClickListener {
            val bundle = bundleOf("recordId" to recordId)
            newSurgeryFragment = NewSurgeryFragment()
            newSurgeryFragment.arguments = bundle
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigationFragmentContainer, newSurgeryFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }
    }

    private fun adjustViewForVet(){
        var stringVet = SharedPreferencesUtil.getVetData()
        if(!stringVet.isNullOrEmpty()) {
            binding.apply {
                topAppBar.setBackgroundColor(resources.getColor(R.color.vet_blue))
                backgroundLayout.background = resources.getDrawable(R.drawable.generic_background_vet)
                addNewSurgeryButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }
}