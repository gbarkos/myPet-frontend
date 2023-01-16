package com.example.mypet.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.mypet.R

class MissingPetDialog : DialogFragment() {
    private lateinit var missingFragment: SetMissingFragment

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Πρόκειται να δηλώσετε το κατοικίδιό σας ως εξαφανισμένο. Είστε σίγουροι;")
                .setPositiveButton("Ναι",
                    DialogInterface.OnClickListener { dialog, id ->
                        println("Dialog accepted")
                        missingFragment = SetMissingFragment()
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.navigationFragmentContainer, missingFragment)

                        transaction?.commit()
                    })
                .setNegativeButton("Οχι",
                    DialogInterface.OnClickListener { dialog, id ->
                       println("Dialog canceled")
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}