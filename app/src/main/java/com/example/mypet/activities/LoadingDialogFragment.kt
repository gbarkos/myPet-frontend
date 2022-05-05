package com.example.mypet.activities

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.mypet.R

class LoadingDialogFragment {

   /* override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_progress_bar, container, false)
    }*/

    var activity : FragmentActivity?
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
    }
}