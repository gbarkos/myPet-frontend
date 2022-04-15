package com.example.mypet.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
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
import com.example.mypet.databinding.FragmentPetInfoBinding
import com.example.mypet.models.Pet
import com.example.mypet.utils.MongoDateAdapter
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.utils.EventObserver
import com.example.mypet.utils.NetworkLoadingState
import com.example.mypet.viewmodels.PetsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PetInfoFragment: Fragment(R.layout.fragment_pet_info) {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPetInfoBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        //binding.petsviewmodelInfo = viewmodel

        val petID = viewmodel._id;
        viewmodel.requestPet(petID.toString())

        binding.generateQrCodeButton.setOnClickListener {
            var dialog = QRCodeDialogFragment()
            dialog.show(parentFragmentManager, "qrcode")
        }

        viewmodel.getPetDataFromRepo().observe(viewLifecycleOwner) {
            if (it.pet != null) {
                populateViews(it)
                viewmodel.recordId =
                    it.pet.medicalRecord._id // set the record id to the viewmodel so that patch requests know which pet to update
            }
        }

    }

    private fun populateViews(it: PetGetResponse){
        binding.apply{
            petInfoName.text = it.pet.name.toEditable();
            petInfoBirthdate.text = MongoDateAdapter(it.pet.birthdate).getDate().toEditable()
            petInfoSex.text = it.pet.sex.toEditable()
            petInfoBreed.text = it.pet.breed.toEditable()
            petInfoID.text = it.pet.id?.toEditable() ?: "Δεν έχει καταχωρηθεί στο μητρώο".toEditable();
            petInfoColor.text = it.pet.colour.toEditable()
            petInfoHeight.text = it.pet.height?.toEditable() ?: "-".toEditable()
            petInfoWeight.text = it.pet.weight?.toEditable() ?: "-".toEditable()
            petInfoDistinguishingMarks.text = it.pet.distinguishingMarks?.toEditable() ?: "-".toEditable()
        }
    }
}

    private fun String.toEditable(): Editable? {
        return Editable.Factory.getInstance().newEditable(this)
    }


