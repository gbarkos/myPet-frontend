package com.example.mypet.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypet.R
import com.example.mypet.databinding.FragmentAddPetBinding
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.viewmodels.PetsViewModel

class NewPetFragment : Fragment(R.layout.fragment_add_pet), AuthFunctions {

    private lateinit var binding: FragmentAddPetBinding;
    private lateinit var viewmodel: PetsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAddPetBinding.bind(view); //viewbinding
        viewmodel = ViewModelProvider(this)[PetsViewModel::class.java];
        binding.newPetViewmodel = viewmodel //databinding
        viewmodel.authListener = this   //assign authlistener
    }


    override fun OnStarted() {
        /*Toast.makeText(requireContext(),"Registering...", Toast.LENGTH_LONG).show()
        binding.registerButton.setEnabled(false)
        binding.textViewUsernameError.visibility = View.INVISIBLE
        binding.textViewEmailError.visibility = View.INVISIBLE
        binding.textViewPhoneNumberError.visibility = View.INVISIBLE
        binding.textViewNameError.visibility = View.INVISIBLE
        binding.textViewSurnameError.visibility = View.INVISIBLE
        binding.textViewPasswordError.visibility = View.INVISIBLE
        binding.textViewConfirmPasswordError.visibility = View.INVISIBLE
        binding.textViewAddressError.visibility = View.INVISIBLE
        Log.d("register fragment", "Signing up...")*/
    }

    override fun OnSuccess() {
        Log.d("RegisterFragment", "Succeed..")

        /*viewmodel.getUserRegisterDataFromRepo().observe(requireActivity(), {
            Log.i("VIEWMODEL", "OnSuccess: ${it?.token}")
            Toast.makeText(requireContext(),"Signed up!", Toast.LENGTH_LONG).show()
            navRegister()
        })
        viewmodel.getFailureMessageFromRegister().observe(requireActivity(), {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        })
        binding.registerButton.setEnabled(true)*/
    }

    override fun OnFailure(errorCodes: MutableList<Int>) {
        /*binding.registerButton.setEnabled(true)
        for(error in errorCodes) {
            if (error == 910) {
                binding.textViewUsernameError.visibility = View.VISIBLE
                binding.textViewUsernameError.text = getString(R.string.username_not_valid_error)
            }
            if (error == 911){
                binding.textViewUsernameError.visibility = View.VISIBLE
                binding.textViewUsernameError.text = getString(R.string.field_required)
            }
            if (error == 920){
                binding.textViewEmailError.visibility = View.VISIBLE
                binding.textViewEmailError.text = getString(R.string.email_not_valid_error)
            }
            if (error == 921){
                binding.textViewEmailError.visibility = View.VISIBLE
                binding.textViewEmailError.text = getString(R.string.field_required)
            }
            if (error == 930){
                binding.textViewPhoneNumberError.visibility = View.VISIBLE
                binding.textViewPhoneNumberError.text = getString(R.string.phone_number_not_valid_error)
            }
            if (error == 931){
                binding.textViewPhoneNumberError.visibility = View.VISIBLE
                binding.textViewPhoneNumberError.text = getString(R.string.field_required)
            }
            if (error == 940){
                binding.textViewNameError.visibility = View.VISIBLE
                binding.textViewNameError.text = getString(R.string.field_required)
            }
            if (error == 950){
                binding.textViewSurnameError.visibility = View.VISIBLE
                binding.textViewSurnameError.text = getString(R.string.field_required)
            }
            if (error == 960){
                binding.textViewPasswordError.visibility = View.VISIBLE
                binding.textViewPasswordError.text = getString(R.string.password_length_error)
            }
            if (error == 961){
                binding.textViewConfirmPasswordError.visibility = View.VISIBLE
                binding.textViewConfirmPasswordError.text = getString(R.string.password_not_match_error)
            }
            if (error == 70){
                binding.textViewAddressError.visibility = View.VISIBLE
                binding.textViewAddressError.text = getString(R.string.field_required)
            }
        }*/
    }
}