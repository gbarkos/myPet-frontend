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
import com.example.mypet.databinding.FragmentLoginVetBinding
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.UserAuthViewModel
import com.example.mypet.viewmodels.VetViewModel

class VetLoginFragment: Fragment(R.layout.fragment_login_vet), AuthFunctions {
    private lateinit var binding: FragmentLoginVetBinding;
    private lateinit var viewmodel: VetViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLoginVetBinding.bind(view); //viewbinding
        viewmodel = ViewModelProvider(this)[VetViewModel::class.java];
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )
        binding.vetloginviewmodel = viewmodel //databinding
        viewmodel.authListener = this   //assign authlistener

        binding.textViewGoToUserLogin.setOnClickListener() {
            navRegister()
        }
    }

    private fun navRegister() {
        findNavController().navigate(R.id.action_vetLoginFragment_to_userLoginFragment)
    }

    override fun OnStarted() {
        binding.textViewError.visibility = View.INVISIBLE
        Log.d("Login fragment", "Logging in...")
        //Toast.makeText(context, "Login", Toast.LENGTH_LONG).show()
    }

    override fun OnSuccess() {
        Log.d("Login fragment", "Succeed")

        viewmodel.getVetLoginDataFromRepo().observe(requireActivity(), {
            SharedPreferencesUtil.saveAccessToken(it?.token.toString())
            val intent = Intent(activity, MainContentVetActivity::class.java)
            startActivity(intent)
            Toast.makeText(context, "Success...", Toast.LENGTH_LONG).show()
        })
    }

    override fun OnFailure(errorCode: MutableList<Int>) {
        Log.d("Login fragment", "Wrong username or password")
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.setText("Λάθος όνομα χρήστη ή κωδικός πρόσβασης")
        //Toast.makeText(context, "Wrong username or password...", Toast.LENGTH_LONG).show()
    }

}