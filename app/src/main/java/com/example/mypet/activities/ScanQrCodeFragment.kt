package com.example.mypet.activities

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentLoginVetBinding
import com.example.mypet.databinding.FragmentScanQrCodeBinding
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.MedicalRecordViewModel
import com.example.mypet.viewmodels.VetViewModel
import com.google.gson.Gson

class ScanQrCodeFragment: Fragment(R.layout.fragment_scan_qr_code), AuthFunctions {
    private lateinit var binding: FragmentScanQrCodeBinding
    private lateinit var viewmodel: VetViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentScanQrCodeBinding.bind(view); //viewbinding
        viewmodel = ViewModelProvider(requireActivity())[VetViewModel::class.java]
        viewmodel.authListener = this
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )

        viewmodel.getVetProfile()

        binding.scanQrCodeButton.setOnClickListener() {
            findNavController().navigate(R.id.action_scanQrCodeFragment_to_qrCodeScannerFragment)
        }
    }

    override fun OnStarted() {
    }

    override fun OnSuccess() {
       viewmodel.getVetProfileDataFromRepo().observe(viewLifecycleOwner){
           var gson = Gson()
           var jsonVet = gson.toJson(it?.vet)
           SharedPreferencesUtil.saveVetData(jsonVet)
       }
    }

    override fun OnFailure(errorCode: MutableList<Int>?) {
    }
}