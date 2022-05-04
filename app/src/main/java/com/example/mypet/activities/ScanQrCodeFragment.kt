package com.example.mypet.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentLoginVetBinding
import com.example.mypet.databinding.FragmentScanQrCodeBinding
import com.example.mypet.viewmodels.VetViewModel

class ScanQrCodeFragment: Fragment(R.layout.fragment_scan_qr_code) {
    private lateinit var binding: FragmentScanQrCodeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentScanQrCodeBinding.bind(view); //viewbinding

        binding.scanQrCodeButton.setOnClickListener() {
            findNavController().navigate(R.id.action_scanQrCodeFragment_to_qrCodeScannerFragment)
        }
    }
}