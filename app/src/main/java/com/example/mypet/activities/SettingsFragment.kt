package com.example.mypet.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentSettingsBinding
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.viewmodels.UserViewModel

class SettingsFragment: Fragment(R.layout.fragment_settings), ResponseFunctions {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewmodel: UserViewModel
    lateinit var dialog : LoadingCircleDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding.userviewmodel = viewmodel
        viewmodel.responseListener = this   //assign responseListener
        dialog = LoadingCircleDialog()

        viewmodel.getProfile()
        viewmodel.getUserProfileDataFromRepo().observe(requireActivity(), {
            while(it == null){

            }
            Log.d("SettingsFragment",it?.user.toString())
            print(it?.user)
            binding.receiveMailCheckBox.isChecked = it?.user!!.receiveMissingMail
        })

        binding.settingsReturn.setOnClickListener {
            val intent = Intent(activity, MainContentActivity::class.java)
            startActivity(intent)
        }

        binding.settingsSave.setOnClickListener {
            viewmodel.updateUserEmailPreferences(binding.receiveMailCheckBox.isChecked)
        }
    }

    override fun OnStarted() {
        dialog.show(parentFragmentManager, "")
    }

    override fun OnSuccess() {
        dialog.dismiss()
        viewmodel.getUserUpdateDataFromRepo().observe(requireActivity(), {
            binding.receiveMailCheckBox.isChecked = it?.user!!.receiveMissingMail
            Toast.makeText(context, "Αποθηκεύτηκε!", Toast.LENGTH_LONG).show()
        })
    }

    override fun OnFailure(errorMsg: String?) {
        dialog.dismiss()
        Toast.makeText(context, "Could not perform update. Server error", Toast.LENGTH_LONG).show()
    }
}