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
import com.example.mypet.models.enums.RegisterFormFields
import com.example.mypet.models.maps.RegisterErrorMap
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


        viewmodel.getRegisterFormValidation().observe(viewLifecycleOwner){
            if(it != null){
                binding.usernameInput.error = if(it.hasUsernamePassedValidation) "" else RegisterErrorMap.ErrorMap[it.usernameError]
                binding.emailInput.error = if(it.hasEmailPassedValidation) "" else RegisterErrorMap.ErrorMap[it.emailError]
                binding.passwordInput.error = if(it.hasPasswordPassedValidation) "" else RegisterErrorMap.ErrorMap[it.passwordError]
                binding.confirmPasswordInput.error = if(it.hasConfirmPasswordPassedValidation) "" else RegisterErrorMap.ErrorMap[it.confirmPasswordError]
            }
        }

        // Listener declaration
        binding.textViewGoToLogin.setOnClickListener {
            navRegister()
        }

        binding.registerButton.setOnClickListener {
            viewmodel.onRegisterButtonClick(binding.registerUsername.text.toString(), binding.registerEmail.text.toString(),
            binding.registerPassword.text.toString(), binding.registerConfirmPassword.text.toString())
        }

        binding.registerUsername.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus){
                viewmodel.validateRegisterFormField(RegisterFormFields.Username, binding.registerUsername.text.toString())
            }
        }
        binding.registerEmail.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus){
                viewmodel.validateRegisterFormField(RegisterFormFields.Email, binding.registerEmail.text.toString())
            }
        }
        binding.registerPassword.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus){
                viewmodel.validateRegisterFormField(RegisterFormFields.Password, binding.registerPassword.text.toString())
            }
        }
        binding.registerConfirmPassword.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus){
                viewmodel.validateRegisterFormField(RegisterFormFields.ConfirmPassword, binding.registerConfirmPassword.text.toString())
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
        Toast.makeText(requireContext(),"Επιτυχής εγγραφή!", Toast.LENGTH_LONG).show()
        loadingDialog.dismiss()
        navRegister()
        binding.registerButton.setEnabled(true)
    }

    override fun OnFailure(errorCodes: String?) {
        loadingDialog.dismiss()
        binding.registerButton.setEnabled(true)
        Toast.makeText(requireContext(),"Σφάλμα εγγραφής", Toast.LENGTH_LONG).show()
    }
}