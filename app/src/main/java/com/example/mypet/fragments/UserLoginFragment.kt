package com.example.mypet.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentLoginUserBinding
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.UserAuthViewModel

class UserLoginFragment: Fragment(R.layout.fragment_login_user), AuthFunctions {
    private lateinit var binding: FragmentLoginUserBinding;
    private lateinit var viewmodel: UserAuthViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLoginUserBinding.bind(view); //viewbinding
        viewmodel = ViewModelProvider(this)[UserAuthViewModel::class.java];
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding.userloginviewmodel = viewmodel //databinding
        viewmodel.authListener = this   //assign authlistener

        binding.textViewGoToRegister.setOnClickListener() {
            navRegister()
        }
    }

    private fun navRegister() {
        findNavController().navigate(R.id.action_userLoginFragment_to_userRegisterFragment)
    }

    override fun OnStarted() {
        //binding.textViewError.visibility = View.INVISIBLE TODO("Not yet implemented")
        Log.d("Login fragment", "Logging in...")
        //toast("Login...", activity)

    }

    override fun OnSuccess() {
        Log.d("Login fragment", "Succeed")

        viewmodel.getUserLoginDataFromRepo().observe(requireActivity(), {
            SharedPreferencesUtil.saveAccessToken(it?.token.toString())
            //val intent = Intent(activity, BottomNavigationActivity::class.java) //TODO("Not yet implemented")
            //startActivity(intent)
            Toast.makeText(context, "Success...", Toast.LENGTH_LONG).show()
        })
    }

    override fun OnFailure(errorCode: MutableList<Int>) {
        Log.d("Login fragment", "Wrong Id or Password")
        //binding.textViewError.visibility = View.VISIBLE
        //binding.textViewError.setText("Wrong Health Identification Number or Password.")
        //toast("Wrong Health Id or Password", activity)
        Toast.makeText(context, "Wrong username or password...", Toast.LENGTH_LONG).show()
    }

}