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

class MissingPetSubmitDialog : DialogFragment(), ResponseFunctions {

    lateinit var dialog : LoadingCircleDialog
    private lateinit var viewmodel: PetsViewModel
    private lateinit var petInfoFragment: PetInfoFragment

    private lateinit var contactInfo: List<String>
    private lateinit var lat: String
    private lateinit var lng: String
    private var petId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        contactInfo = arguments?.getStringArrayList(CONTACT_INFO) ?: throw IllegalStateException("No args provided")
        lat = arguments?.getString(LAT) ?: throw IllegalStateException("No args provided")
        lng = arguments?.getString(LNG) ?: throw IllegalStateException("No args provided")
        petId = arguments?.getString(PET_ID) ?: throw IllegalStateException("No args provided")
        dialog = LoadingCircleDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewmodel = ViewModelProvider(requireActivity())[PetsViewModel::class.java]
        viewmodel.responseListener = this

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Με την υποβολή, συναινείτε στην κοινοποίηση των στοιχείων επικοινωνίας σας σε τρίτους. Θέλετε να συνεχίσετε;")
                .setPositiveButton("Ναι",
                    DialogInterface.OnClickListener { dialog, id ->
                        viewmodel.setPetAsMissing(lat, lng, contactInfo, petId)
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

        private const val CONTACT_INFO = "contactInfo"
        private const val LAT = "lat"
        private const val LNG = "lng"
        private const val PET_ID = "petId"

        fun newInstance(
            contactInfo: ArrayList<String>,
            lat: String,
            lng: String,
            petId: String?
        ): MissingPetSubmitDialog = MissingPetSubmitDialog().apply {
            arguments = Bundle().apply {
                putStringArrayList(CONTACT_INFO, contactInfo)
                putString(LAT, lat)
                putString(LNG, lng)
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
        Toast.makeText(context, "Καταχωρήθηκε!", Toast.LENGTH_LONG).show()
    }

    override fun OnFailure(errorMsg: String?) {
        Toast.makeText(context, "Σφάλμα καταχώρησης!", Toast.LENGTH_LONG).show()
    }
}