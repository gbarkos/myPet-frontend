package com.example.mypet.fragments

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentLoginUserBinding
import com.example.mypet.viewmodels.UserAuthViewModel

class UserLoginFragment: Fragment(R.layout.fragment_login_user) {
    private lateinit var binding: FragmentLoginUserBinding;
    private lateinit var viewmodel: UserAuthViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLoginUserBinding.bind(view);
        viewmodel = ViewModelProvider(this)[UserAuthViewModel::class.java];
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding.loginviewmodel = viewmodel
    }

}