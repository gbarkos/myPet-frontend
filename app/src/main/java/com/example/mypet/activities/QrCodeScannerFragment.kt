package com.example.mypet.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.mypet.R
import com.example.mypet.databinding.FragmentQrCodeScannerBinding

class QrCodeScannerFragment : Fragment(R.layout.fragment_qr_code_scanner){

    private lateinit var codeScanner : CodeScanner
    private lateinit var binding: FragmentQrCodeScannerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQrCodeScannerBinding.bind(view)

        if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            permReqLauncher.launch(android.Manifest.permission.CAMERA)
        }else {
            startScanning()
        }
    }

    private fun startScanning(){
        val scannerView: CodeScannerView = binding.scannerView
        codeScanner = CodeScanner(requireContext(), scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            Handler(Looper.getMainLooper()).post {
                val intent = Intent (getActivity(), PetDetailsActivity::class.java)
                val bundle = bundleOf("petID" to it.text)
                intent.putExtras(bundle)
                activity?.startActivity(intent)
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
        }

        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized){
            codeScanner?.startPreview()
        }
    }

    override fun onPause(){
        if(::codeScanner.isInitialized){
            codeScanner?.releaseResources()
        }
            super.onPause()
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startScanning()
            }
        }
}