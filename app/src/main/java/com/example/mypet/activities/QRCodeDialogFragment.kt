package com.example.mypet.activities

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentQrCodeDialogBinding
import com.example.mypet.viewmodels.PetsViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QRCodeDialogFragment : DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var rootview = inflater.inflate(R.layout.fragment_qr_code_dialog, container, false)
        var binding: FragmentQrCodeDialogBinding = FragmentQrCodeDialogBinding.bind(rootview)
        var viewmodel: PetsViewModel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(viewmodel.getPetObj()?._id ?: "No valid pet", BarcodeFormat.QR_CODE, 750, 750)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }

        binding.qrCodeContent.setImageBitmap(bitmap)

        return rootview
    }
}