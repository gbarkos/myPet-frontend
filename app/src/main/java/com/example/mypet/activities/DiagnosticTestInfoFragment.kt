package com.example.mypet.activities

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetDiagnosticTestBinding
import com.example.mypet.databinding.FragmentPetSurgeryBinding
import com.example.mypet.databinding.FragmentPetTreatmentBinding
import com.example.mypet.databinding.FragmentPetVermifugationBinding
import com.example.mypet.models.DiagnosticTest
import com.example.mypet.models.Surgery
import com.example.mypet.models.Treatment
import com.example.mypet.models.Vermifugation
import com.example.mypet.utils.MongoDateAdapter
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.PetsViewModel

class DiagnosticTestInfoFragment  : Fragment(R.layout.fragment_pet_diagnostic_test){
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetDiagnosticTestBinding
    private lateinit var test: DiagnosticTest
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentPetDiagnosticTestBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsViewmodelDiagnosticTest= viewmodel
        val id = arguments?.getString("treatmentID")
        adjustViewForVet()
        //val petID = viewmodel._id;
        //Toast.makeText(context, "$petID", Toast.LENGTH_LONG).show()

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner, {
            Log.d("Pet!!!",it.pet.toString())
            for(item in it.pet.medicalRecord.diagnosticTests){
                if(item._id.equals(id)){
                    test = item
                    break;
                }
            }
            populateViews(test)
        })
    }

    fun populateViews(it: DiagnosticTest){
        binding.apply{
            petDiagnosticTestName.text = it.name.toEditable()
            petDiagnosticTestDate.text = MongoDateAdapter(it.date).getDate().toEditable()
            petDiagnosticTestResult.text = it.result.toEditable()
            petDiagnosticTestVet.text =
                if(it.veterinarian != null) (it.veterinarian.surname+" "+it.veterinarian.name).toEditable()
                else "-".toEditable()
        }
    }

    private fun adjustViewForVet(){
        var stringVet = SharedPreferencesUtil.getVetData()
        if(!stringVet.isNullOrEmpty()) {
            binding.apply {
                topAppBar.setBackgroundColor(resources.getColor(R.color.vet_blue))
                backgroundLayout.background = resources.getDrawable(R.drawable.generic_background_vet)
            }
        }
    }
}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}