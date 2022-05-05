package com.example.mypet.activities

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
    lateinit var dialog : testDialog

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
        dialog = testDialog()

        binding.textViewGoToRegister.setOnClickListener() {
            navRegister()
        }

        binding.textViewGoToVetLogin.setOnClickListener() {
            navVetLogin()
        }
    }

    private fun navRegister() {
        findNavController().navigate(R.id.action_userLoginFragment_to_userRegisterFragment)
    }

    private fun navVetLogin(){
        findNavController().navigate(R.id.action_userLoginFragment_to_vetLoginFragment)
    }

    override fun OnStarted() {
        binding.textViewError.visibility = View.INVISIBLE
        Log.d("Login fragment", "Logging in...")
        dialog.show(parentFragmentManager, "")
        //Toast.makeText(context, "Login", Toast.LENGTH_LONG).show()
    }

    override fun OnSuccess() {
        Log.d("Login fragment", "Succeed")
        dialog.dismiss()
        viewmodel.getUserLoginDataFromRepo().observe(requireActivity(), {
            SharedPreferencesUtil.saveAccessToken(it?.token.toString())
            val intent = Intent(activity, MainContentActivity::class.java)
            startActivity(intent)

            Toast.makeText(context, "Success...", Toast.LENGTH_LONG).show()
        })
    }

    override fun OnFailure(errorCode: MutableList<Int>) {
        Log.d("Login fragment", "Wrong username or password")
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.setText("Λάθος όνομα χρήστη ή κωδικός πρόσβασης")
        dialog.dismiss()
        //Toast.makeText(context, "Wrong username or password...", Toast.LENGTH_LONG).show()
    }

}