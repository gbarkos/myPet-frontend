package com.example.mypet.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentMissingPetBinding
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.viewmodels.PetsViewModel

class SetMissingFragment : Fragment(R.layout.fragment_missing_pet), ResponseFunctions {
    private lateinit var binding: FragmentMissingPetBinding
    private lateinit var viewmodel: PetsViewModel
    lateinit var dialog : LoadingCircleDialog
    lateinit var missingDialog : MissingPetSubmitDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMissingPetBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsviewmodelMissing = viewmodel
        viewmodel.responseListener = this   //assign responseListener
        dialog = LoadingCircleDialog()

        binding.submitMissingBtn.setOnClickListener {
            var cellphone = binding.mobileInput.text.toString()
            var phone = binding.phoneInput.text.toString()
            var email = binding.emailInput.text.toString()
            var fb = binding.facebookInput.text.toString()
            var instagram = binding.instagramInput.text.toString()

            if(!isContactInfoFilled(email,cellphone,phone,fb,instagram)){

                binding.errorLabel.text = "Παρακαλώ εισάγετε τουλάχιστον 1 μέσο επικοινωνίας"

            }else{

                val contactInfo : ArrayList<String> = arrayListOf()
                if(email != null) contactInfo.add(email)
                if(cellphone != null) contactInfo.add(cellphone)
                if(phone != null) contactInfo.add(phone)
                if(fb != null) contactInfo.add(fb)
                if(instagram != null) contactInfo.add(instagram)

                missingDialog = MissingPetSubmitDialog.newInstance(contactInfo, "36.8605835", "10.3215698", viewmodel._id)
                missingDialog.show(childFragmentManager, "dialog")
            }
        }

    }

    private fun isContactInfoFilled(email : String, cell : String, phone: String, fb: String, insta: String) : Boolean{

        if(email.isNullOrEmpty() && cell.isEmpty() && phone.isNullOrEmpty() && insta.isNullOrEmpty() && fb.isNullOrEmpty())
            return false

        return true
    }

    override fun OnStarted() {
        TODO("Not yet implemented")
    }

    override fun OnSuccess() {
        TODO("Not yet implemented")
    }

    override fun OnFailure(errorCode: MutableList<Int>?) {
        TODO("Not yet implemented")
    }
}