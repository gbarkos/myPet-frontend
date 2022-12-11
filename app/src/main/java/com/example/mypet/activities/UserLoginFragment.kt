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
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.utils.SharedPreferencesUtil
import com.example.mypet.viewmodels.UserViewModel

class UserLoginFragment: Fragment(R.layout.fragment_login_user), ResponseFunctions {
    private lateinit var binding: FragmentLoginUserBinding;
    private lateinit var viewmodel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var dialog : LoadingCircleDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLoginUserBinding.bind(view); //viewbinding
        viewmodel = ViewModelProvider(this)[UserViewModel::class.java];
        sharedPreferences = requireActivity().getSharedPreferences(
            requireActivity().packageName,
            Activity.MODE_PRIVATE
        )

        binding.userloginviewmodel = viewmodel //databinding
        viewmodel.responseListener = this   //assign responseListener
        dialog = LoadingCircleDialog()

        viewmodel.getStatusFromLoginValidation().observe(viewLifecycleOwner) {
            if(it != null){
                binding.textViewError.text = it
            }
        }

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
            SharedPreferencesUtil.saveUsername(it?.user!!.username)
            SharedPreferencesUtil.savePassword(it?.user!!.password)
            SharedPreferencesUtil.deleteVetData()
            val intent = Intent(activity, MainContentActivity::class.java)
            startActivity(intent)

            Toast.makeText(context, "Επιτυχής σύνδεση", Toast.LENGTH_LONG).show()
        })
    }

    override fun OnFailure(errorMsg: String?) {
        Log.d("Login fragment", "Wrong username or password")
        binding.textViewError.visibility = View.VISIBLE
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        //binding.textViewError.setText(errorMsg)
        dialog.dismiss()
    }
}