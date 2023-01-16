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
import com.example.mypet.adapters.VaccinationsAdapter
import com.example.mypet.databinding.FragmentVaccinationsBinding
import com.example.mypet.models.Vaccination
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class VaccinationsFragment : Fragment(R.layout.fragment_vaccinations){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentVaccinationsBinding
    private val vaccinationInfoFragment = VaccinationInfoFragment()
    private lateinit var newVaccinationFragment : NewVaccinationFragment
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var vaccinationsList: List<Vaccination>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentVaccinationsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsviewmodel = viewmodel

        adjustViewForVet()

        val petID = viewmodel._id
        val recordId = viewmodel.recordId
        val receivedBundle = arguments?.getBoolean("recordHasBeenUpdated")
        if(receivedBundle == true) { // if there's an update on the medical record, re-fetch the data
            if(petID != null){
                viewmodel.requestPet(petID)
            }
        }

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pet!!!",it.pet.toString())
            vaccinationsList = it.pet.medicalRecord.vaccinations
            binding.recyclerViewVaccinations.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = VaccinationsAdapter(vaccinationsList,lifecycleScope)
            }

            (binding.recyclerViewVaccinations.adapter as VaccinationsAdapter).onClick.onEach {
                Log.d("OnItemClick",it._id)
                vaccinationInfoFragment.arguments = bundleOf("vaccinationID" to it._id)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(this.id, vaccinationInfoFragment)
                transaction.commit()

            }.launchIn(lifecycleScope)
        })

        // send the pet id so that the addVaccination request knows what to update
        binding.addNewVaccinationButton.setOnClickListener {
            val bundle = bundleOf("recordId" to recordId)
            newVaccinationFragment = NewVaccinationFragment()
            newVaccinationFragment.arguments = bundle
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigationFragmentContainer, newVaccinationFragment)
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
                addNewVaccinationButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }

}