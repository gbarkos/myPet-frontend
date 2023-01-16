package com.example.mypet.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.viewmodels.RemindersViewModel

class ConfirmReminderDeleteDialog : DialogFragment(), ResponseFunctions {
    lateinit var dialog : LoadingCircleDialog
    private lateinit var viewmodel: RemindersViewModel
    private lateinit var reminderId: String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        reminderId = arguments?.getString(REMINDER_ID) ?: throw IllegalStateException("No args provided")
        dialog = LoadingCircleDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewmodel = ViewModelProvider(requireActivity())[RemindersViewModel::class.java]
        viewmodel.responseListener = this

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε αυτήν την υπνεθύμιση;")
                .setPositiveButton("Ναι",
                    DialogInterface.OnClickListener { dialog, id ->
                        // TODO
                        viewmodel.deleteReminder(reminderId)
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

        private const val REMINDER_ID = "reminderId"

        fun newInstance(
            reminderId: String,
        ): ConfirmReminderDeleteDialog = ConfirmReminderDeleteDialog().apply {
            arguments = Bundle().apply {
                putString(REMINDER_ID, reminderId)
            }
        }
    }

    override fun OnStarted() {
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        dialog.dismiss()
        Toast.makeText(context, "Επιτυχής διαγραφή!", Toast.LENGTH_LONG).show()
    }

    override fun OnFailure(errorMsg: String?) {
        dialog.dismiss()
        Toast.makeText(context, "Σφάλμα διαγραφής!", Toast.LENGTH_LONG).show()
    }
}
