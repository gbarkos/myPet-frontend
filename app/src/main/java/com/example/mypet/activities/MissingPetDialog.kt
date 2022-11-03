package com.example.mypet.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.mypet.R

class MissingPetDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Πρόκειται να δηλώσετε το κατοικίδιό σας ως εξαφανισμένο. Είστε σίγουροι;")
                .setPositiveButton("Ναι",
                    DialogInterface.OnClickListener { dialog, id ->
                        println("Dialog accepted")
                    })
                .setNegativeButton("Οχι",
                    DialogInterface.OnClickListener { dialog, id ->
                       println("Dialog canceled")
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /*var activity : FragmentActivity?
    lateinit var dialog : AlertDialog

    constructor(myActivity: FragmentActivity?){
        activity = myActivity
    }

    fun startLoadingDialog(){
        var builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater : LayoutInflater = activity!!.layoutInflater
        builder.setView(inflater.inflate(R.layout.fragment_progress_bar, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }*/
}