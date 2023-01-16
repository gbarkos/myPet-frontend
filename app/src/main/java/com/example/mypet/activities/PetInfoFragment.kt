package com.example.mypet.activities

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetInfoBinding
import com.example.mypet.models.Vet
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.utils.*
import com.example.mypet.viewmodels.PetsViewModel
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class PetInfoFragment: Fragment(R.layout.fragment_pet_info) {
    private lateinit var viewmodel: PetsViewModel
    private lateinit var binding: FragmentPetInfoBinding
    private lateinit var editPetFragment : EditPetFragment
    lateinit var foundDialog : FoundPetDialog
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding = FragmentPetInfoBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]

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
                viewmodel.isMissing = it.pet.isMissing
            }
        }

        binding.missingPet.setOnClickListener{
            if(viewmodel.isMissing){
                foundDialog = FoundPetDialog.newInstance(petID)
                foundDialog.show(childFragmentManager, "dialog")
            }else{
                var dialog : MissingPetDialog = MissingPetDialog()
                dialog.show(this.parentFragmentManager, "Missing pet")
            }
        }
    }

    private fun populateViews(it: PetGetResponse){
        var stringVet = SharedPreferencesUtil.getVetData()
        var vet : Vet? = null
        if(!stringVet.isNullOrEmpty()) {
            var gson = Gson()
            vet = gson.fromJson(stringVet, Vet::class.java)
        }

        binding.apply{
            petInfoName.text = it.pet.name.toEditable();
            petInfoBirthdate.text = MongoDateAdapter(it.pet.birthdate).getDate().toEditable()
            petInfoSex.text = it.pet.sex.toEditable()
            petInfoSpecies.text = it.pet.species.toEditable()
            petInfoBreed.text = it.pet.breed.toEditable()
            petInfoID.text = it.pet.id?.toEditable() ?: "Δεν έχει καταχωρηθεί στο μητρώο".toEditable();
            petInfoColor.text = it.pet.colour.toEditable()
            petInfoHeight.text = it.pet.height?.toEditable() ?: "-".toEditable()
            petInfoWeight.text = it.pet.weight?.toEditable() ?: "-".toEditable()
            petInfoDistinguishingMarks.text = it.pet.distinguishingMarks?.toEditable() ?: "-".toEditable()
            if(it.pet.isMissing) {
                cardView.strokeWidth = 5
                val colorInt = Color.parseColor("#FF0000")
                cardView.strokeColor = colorInt
            }else{
                cardView.strokeWidth = 0
            }

            if(vet !=null ){
                editPetButton.hide()
                generateQrCodeButton.hide()

                missingPet.setImageResource(0)
                missingPet.setOnClickListener(null)
                topAppBar.setBackgroundColor(resources.getColor(R.color.vet_blue))
                backgroundLayout.background = resources.getDrawable(R.drawable.generic_background_vet)
                constraintLayout2.setBackgroundColor(resources.getColor(R.color.vet_blue))
                imageCardView.setStrokeColor(resources.getColor(R.color.vet_blue_light))
            }
        }

        var picasso = Picasso.get()
        picasso.isLoggingEnabled = true

        picasso
            .load("https://drive.google.com/uc?export=download&id="+it.pet.photo)
            .config(Bitmap.Config.RGB_565)
            .error(R.drawable.palceholder_profile_pic)
            .into(binding.petInfoProfilePic)
    }
}

private fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}


