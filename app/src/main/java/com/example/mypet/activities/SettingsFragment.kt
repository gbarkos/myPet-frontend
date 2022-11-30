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
import com.example.mypet.viewmodels.UserAuthViewModel

class SettingsFragment: Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewmodel: UserAuthViewModel
    lateinit var dialog : testDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)
        viewmodel = ViewModelProvider(requireActivity())[UserAuthViewModel::class.java]
        binding.userviewmodel = viewmodel
        dialog = testDialog()

        binding.receiveMailCheckBox.isChecked = viewmodel.receiveMails!!

        print(viewmodel.receiveMails)

        viewmodel.getUserLoginDataFromRepo().observe(requireActivity(), {
            Log.d("SettingsFragment",it?.user.toString())
            print(it?.user)
           //binding.receiveMailCheckBox.isChecked = it?.user.receiveMissingMail
        })

        binding.settingsReturn.setOnClickListener {
            val intent = Intent(activity, MainContentActivity::class.java)
            startActivity(intent)
        }
    }
}