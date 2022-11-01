package com.example.mypet.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import com.example.mypet.utils.Constants
import com.example.mypet.utils.EventObserver
import com.example.mypet.utils.NetworkLoadingState
import com.example.mypet.viewmodels.PetsViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

class PetInfoFragment: Fragment(R.layout.fragment_pet_info) {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetInfoBinding
    private lateinit var editPetFragment : EditPetFragment

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

        binding.editPetButton.setOnClickListener{
            editPetFragment = EditPetFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.navigationFragmentContainer, editPetFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
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

        var picasso = Picasso.get()
        picasso.isLoggingEnabled = true

        picasso
            .load("http://192.168.1.7:8005/images/pets/"+it.pet.photo)
            .config(Bitmap.Config.RGB_565)
            .resize(160, 160)
            .error(R.drawable.dummy_pet_profile_pic)
            .into(binding.petInfoProfilePic)
    }
}

    private fun String.toEditable(): Editable? {
        return Editable.Factory.getInstance().newEditable(this)
    }


