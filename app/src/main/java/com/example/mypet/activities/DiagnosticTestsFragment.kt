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
import com.example.mypet.adapters.DiagnosticTestsAdapter
import com.example.mypet.adapters.SurgeriesAdapter
import com.example.mypet.adapters.TreatmentsAdapter
import com.example.mypet.adapters.VermifugationsAdapter
import com.example.mypet.databinding.FragmentDiagnosticTestsBinding
import com.example.mypet.databinding.FragmentSurgeriesBinding
import com.example.mypet.databinding.FragmentTreatmentsBinding
import com.example.mypet.databinding.FragmentVermifugationsBinding
import com.example.mypet.models.DiagnosticTest
import com.example.mypet.models.Surgery
import com.example.mypet.models.Treatment
import com.example.mypet.models.Vermifugation
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DiagnosticTestsFragment : Fragment(R.layout.fragment_diagnostic_tests){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentDiagnosticTestsBinding
    private val diagnosticTestInfoFragment = DiagnosticTestInfoFragment()
    private lateinit var newDiagnosticTestFragment : NewDiagnosticTestFragment

    private lateinit var testsList: List<DiagnosticTest>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDiagnosticTestsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsViewmodelDiagnosticTests = viewmodel

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
                //transaction.addToBackStack("null")
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
}