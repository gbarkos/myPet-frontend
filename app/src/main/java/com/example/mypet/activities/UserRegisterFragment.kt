package com.example.mypet.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.databinding.FragmentRegisterUserBinding
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.viewmodels.UserAuthViewModel

class UserRegisterFragment: Fragment(R.layout.fragment_register_user), AuthFunctions {

    private lateinit var binding: FragmentRegisterUserBinding
    private lateinit var viewmodel: UserAuthViewModel
    lateinit var loadingDialog : testDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentRegisterUserBinding.bind(view); //viewbinding
        viewmodel = ViewModelProvider(this)[UserAuthViewModel::class.java];
        binding.userregisterviewmodel = viewmodel //databinding
        viewmodel.authListener = this   //assign authlistener
        loadingDialog = testDialog()
        binding.textViewGoToLogin.setOnClickListener {
            navRegister()
        }

        //errors
        binding.registerUsername.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.usernameInput.error = null
        }
        binding.registerEmail.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.emailInput.error = null
        }
       /* binding.registerName.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.nameInput.error = null
        }
        binding.registerSurname.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.surnameInput.error = null
        }
        binding.registerPhone.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.phoneInput.error = null
        }
        binding.registerAddress.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.addressInput.error = null
        }*/
        binding.registerPassword.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.passwordInput.error = null
        }
        binding.registerConfirmPassword.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty())binding.confirmPasswordInput.error = null
        }
    }

    private fun navRegister() {
        findNavController().navigate(R.id.action_userRegisterFragment_to_userLoginFragment)
    }

    override fun OnStarted() {
        loadingDialog.show(parentFragmentManager, "")
        Toast.makeText(requireContext(),"Registering...", Toast.LENGTH_LONG).show()
        binding.registerButton.setEnabled(false)
        Log.d("register fragment", "Signing up...")
    }

    override fun OnSuccess() {
        Log.d("RegisterFragment", "Succeeded..")
        loadingDialog.dismiss()
        viewmodel.getUserRegisterDataFromRepo().observe(requireActivity(), {
            Log.i("VIEWMODEL", "OnSuccess: ${it?.token}")
            Toast.makeText(requireContext(),"Signed up!", Toast.LENGTH_LONG).show()
            navRegister()
        })
        viewmodel.getFailureMessageFromRegister().observe(requireActivity(), {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        })
        binding.registerButton.setEnabled(true)
    }

    override fun OnFailure(errorCodes: MutableList<Int>) {
        loadingDialog.dismiss()
        binding.registerButton.setEnabled(true)
        for(error in errorCodes) {
            if (error == 910) {
                binding.usernameInput.error = getString(R.string.username_not_valid_error)
            }
            if (error == 911){
                binding.usernameInput.error = getString(R.string.field_required)
            }
            if (error == 920){
                binding.emailInput.error = getString(R.string.email_not_valid_error)
            }
            if (error == 921){
                binding.emailInput.error = getString(R.string.field_required)
            }
           /* if (error == 930){
                binding.phoneInput.error = getString(R.string.phone_number_not_valid_error)
            }
            if (error == 931){
                binding.phoneInput.error = getString(R.string.field_required)
            }
            if (error == 940){
                binding.nameInput.error = getString(R.string.field_required)
            }
            if (error == 950){
                binding.surnameInput.error = getString(R.string.field_required)
            }*/
            if (error == 960){
                binding.passwordInput.error = getString(R.string.password_length_error)
            }
            if (error == 961){
                binding.passwordInput.error = getString(R.string.password_not_match_error)
            }
           /* if (error == 70){
                binding.addressInput.error = getString(R.string.field_required)
            }*/
        }
    }
}