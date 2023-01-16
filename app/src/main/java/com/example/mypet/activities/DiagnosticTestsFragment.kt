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
import com.example.mypet.adapters.DiagnosticTestsAdapter
import com.example.mypet.databinding.FragmentDiagnosticTestsBinding
import com.example.mypet.models.DiagnosticTest
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DiagnosticTestsFragment : Fragment(R.layout.fragment_diagnostic_tests){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentDiagnosticTestsBinding
    private val diagnosticTestInfoFragment = DiagnosticTestInfoFragment()
    private lateinit var newDiagnosticTestFragment : NewDiagnosticTestFragment
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var testsList: List<DiagnosticTest>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentDiagnosticTestsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsViewmodelDiagnosticTests = viewmodel
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
            testsList = it.pet.medicalRecord.diagnosticTests
            binding.recyclerViewSurgeries.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = DiagnosticTestsAdapter(testsList, lifecycleScope)
            }

            (binding.recyclerViewSurgeries.adapter as DiagnosticTestsAdapter).onClick.onEach {
                Log.d("OnItemClick", it._id)
                diagnosticTestInfoFragment.arguments = bundleOf("treatmentID" to it._id)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(this.id, diagnosticTestInfoFragment)
                transaction.commit()

            }.launchIn(lifecycleScope)
        }

        // send the pet id so that the addVaccination request knows what to update
        binding.addNewDiagnosticTestButton.setOnClickListener {
            val bundle = bundleOf("recordId" to recordId)
            newDiagnosticTestFragment = NewDiagnosticTestFragment()
            newDiagnosticTestFragment.arguments = bundle
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigationFragmentContainer, newDiagnosticTestFragment)
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
                addNewDiagnosticTestButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.vet_blue))
            }
        }
    }
}