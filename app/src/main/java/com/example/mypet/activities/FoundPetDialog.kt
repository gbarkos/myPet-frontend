package com.example.mypet.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.viewmodels.PetsViewModel

class FoundPetDialog : DialogFragment(), ResponseFunctions{

    lateinit var dialog : LoadingCircleDialog
    private lateinit var petInfoFragment: PetInfoFragment
    private lateinit var viewmodel: PetsViewModel
    private var petId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        petId = arguments?.getString(PET_ID) ?: throw IllegalStateException("No args provided")
        dialog = LoadingCircleDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        viewmodel.responseListener = this

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Πρόκειται να δηλώσετε πως το κατοικίδιό σας έχει βρεθεί. Είστε σίγουροι;")
                .setPositiveButton("Ναι",
                    DialogInterface.OnClickListener { dialog, id ->
                        viewmodel.setPetAsFound(petId)
                        println("Dialog accepted")
                    })
                .setNegativeButton("Όχι",
                    DialogInterface.OnClickListener { dialog, id ->
                        println("Dialog canceled")
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {

        private const val PET_ID = "petId"

        fun newInstance(
            petId: String?
        ): FoundPetDialog = FoundPetDialog().apply {
            arguments = Bundle().apply {
                putString(PET_ID, petId)
            }
        }
    }

    override fun OnStarted() {
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        dialog.dismiss()
        petInfoFragment = PetInfoFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.navigationFragmentContainer, petInfoFragment)
        transaction?.commit()
        Toast.makeText(context, "Καταχωρήθηκε", Toast.LENGTH_LONG).show()
    }

    override fun OnFailure(errorCode: MutableList<Int>?) {
        Toast.makeText(context, "Σφάλμα καταχώρησης!", Toast.LENGTH_LONG).show()
    }
}