package com.example.mypet.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentMissingPetBinding
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.viewmodels.PetsViewModel

class SetMissingFragment : Fragment(R.layout.fragment_missing_pet), AuthFunctions {
    private lateinit var binding: FragmentMissingPetBinding
    private lateinit var viewmodel: PetsViewModel
    lateinit var dialog : testDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMissingPetBinding.bind(view)
        binding = FragmentMissingPetBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        binding.petsviewmodelMissing = viewmodel
        viewmodel.authListener = this   //assign authlistener
        dialog = testDialog()

        binding.submitMissingBtn.setOnClickListener {
            // open the dialog
        }

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