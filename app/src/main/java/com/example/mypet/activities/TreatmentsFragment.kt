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
import com.example.mypet.adapters.TreatmentsAdapter
import com.example.mypet.adapters.VermifugationsAdapter
import com.example.mypet.databinding.FragmentTreatmentsBinding
import com.example.mypet.databinding.FragmentVermifugationsBinding
import com.example.mypet.models.Treatment
import com.example.mypet.models.Vermifugation
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TreatmentsFragment : Fragment(R.layout.fragment_treatments){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentTreatmentsBinding
    private val treatmentInfoFragment = TreatmentInfoFragment() //TODO
    private lateinit var newTreatmentFragment : NewTreatmentFragment //TODO
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var treatmentsList: List<Treatment>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentTreatmentsBinding.bind(view)
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
            Log.d("Pets!!!",it.pet.toString())
            treatmentsList = it.pet.medicalRecord.treatments
            binding.recyclerViewTreatments.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = TreatmentsAdapter(treatmentsList,lifecycleScope)
            }

            (binding.recyclerViewTreatments.adapter as TreatmentsAdapter).onClick.onEach {
                Log.d("OnItemClick",it._id)
                treatmentInfoFragment.arguments = bundleOf("treatmentID" to it._id) //TODO
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(this.id, treatmentInfoFragment) //TODO
                //transaction.addToBackStack("null")
                transaction.commit()

            }.launchIn(lifecycleScope)
        })

        // send the pet id so that the addVaccination request knows what to update
        binding.addNewTreatmentButton.setOnClickListener {
            val bundle = bundleOf("recordId" to recordId)
            newTreatmentFragment = NewTreatmentFragment() //TODO
            newTreatmentFragment.arguments = bundle //TODO
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigationFragmentContainer, newTreatmentFragment) //TODO
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
                addNewTreatmentButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }
}