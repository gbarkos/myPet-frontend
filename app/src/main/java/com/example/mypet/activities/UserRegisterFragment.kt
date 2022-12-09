package com.example.mypet.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentRegisterUserBinding
import com.example.mypet.models.enums.RegisterErrorCodes
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.viewmodels.UserViewModel

class UserRegisterFragment: Fragment(R.layout.fragment_register_user), ResponseFunctions {

    private lateinit var binding: FragmentRegisterUserBinding
    private lateinit var viewmodel: UserViewModel
    lateinit var loadingDialog : LoadingCircleDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentRegisterUserBinding.bind(view); //viewbinding
        viewmodel = ViewModelProvider(this)[UserViewModel::class.java];
        binding.userregisterviewmodel = viewmodel //databinding
        viewmodel.responseListener = this   //assign responseListener
        loadingDialog = LoadingCircleDialog()
        binding.textViewGoToLogin.setOnClickListener {
            navRegister()
        }

       /* //errors
        binding.registerUsername.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.usernameInput.error = null
        }
        binding.registerEmail.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.emailInput.error = null
        }
        binding.registerPassword.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.passwordInput.error = null
        }
        binding.registerConfirmPassword.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.confirmPasswordInput.error = null
        }*/

        viewmodel.getStatusFromRegisterValidation().observe(viewLifecycleOwner){
            if(it != null){
                clearErrors()
                for(error in it){
                    when (error){
                        RegisterErrorCodes.MandatoryUsername -> binding.usernameInput.error = resources.getString(R.string.field_required)
                        RegisterErrorCodes.MandatoryEmail -> binding.emailInput.error = resources.getString(R.string.field_required)
                        RegisterErrorCodes.MandatoryPassword -> binding.passwordInput.error = resources.getString(R.string.field_required)
                        RegisterErrorCodes.MandatoryConfirmPassword -> binding.confirmPasswordInput.error = resources.getString(R.string.field_required)
                        RegisterErrorCodes.InvalidPassword -> binding.passwordInput.error = resources.getString(R.string.password_length_error)
                        RegisterErrorCodes.InvalidEmail -> binding.emailInput.error = resources.getString(R.string.email_not_valid_error)
                        RegisterErrorCodes.PasswordsDoNotMatch -> binding.passwordInput.error = resources.getString(R.string.password_not_match_error)
                    }
                }
            }
        }
    }

    private fun navRegister() {
        findNavController().navigate(R.id.action_userRegisterFragment_to_userLoginFragment)
    }

    override fun OnStarted() {
        loadingDialog.show(parentFragmentManager, "")
        binding.registerButton.setEnabled(false)
    }

    override fun OnSuccess() {
        Log.d("RegisterFragment", "Succeeded..")

      /*  viewmodel.getUserRegisterDataFromRepo().observe(requireActivity(), {
            Log.i("VIEWMODEL", "OnSuccess: ${it?.token}")
            Toast.makeText(requireContext(),"Επιτυχής εγγραφή!", Toast.LENGTH_LONG).show()
            navRegister()
        })
        viewmodel.getFailureMessageFromRegister().observe(requireActivity(), {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        })*/
        Toast.makeText(requireContext(),"Επιτυχής εγγραφή!", Toast.LENGTH_LONG).show()
        loadingDialog.dismiss()
        navRegister()
        binding.registerButton.setEnabled(true)
    }

    override fun OnFailure(errorCodes: MutableList<Int>?) {
        loadingDialog.dismiss()
        Toast.makeText(requireContext(),"Σφάλμα εγγραφής", Toast.LENGTH_LONG).show()
    }

    private fun clearErrors(){
        binding.usernameInput.error = ""
        binding.emailInput.error = ""
        binding.passwordInput.error =  ""
        binding.confirmPasswordInput.error = ""
    }
}