package com.example.mypet.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.example.mypet.R
import com.example.mypet.databinding.FragmentPetInfoBinding
import com.example.mypet.databinding.FragmentQrCodeDialogBinding
import com.example.mypet.models.Pet
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.viewmodels.PetsViewModel
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QRCodeDialogFragment : DialogFragment(){


    /* The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment

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

    /* The system calls this only when creating the layout in a dialog. */
   /* override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val dialog = super.onCreateDialog(savedInstanceState)
        //dialog.requestWindowFeature(Window.)
        return dialog
    }*/

}