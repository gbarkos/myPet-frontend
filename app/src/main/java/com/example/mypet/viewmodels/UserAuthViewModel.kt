package com.example.mypet.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.UserAuthRepository
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.utils.*
import com.example.mypet.utils.SingleLiveEvent

class UserAuthViewModel: ViewModel() {
    var authListener: AuthFunctions? = null
    private val userAuthRepository: UserAuthRepository = UserAuthRepository


    fun getUserLoginDataFromRepo(): SingleLiveEvent<UserLoginRegisterPostResponse> {
        return userAuthRepository.getUserLoginResponse()
    }

    fun getUserRegisterDataFromRepo(): SingleLiveEvent<UserLoginRegisterPostResponse> {
        return userAuthRepository.getUserRegisterResponse()
    }

    fun getFailureMessageFromRegister(): SingleLiveEvent<String>{
        return userAuthRepository.getFailureMessageFromRegister()
    }

    fun getStatusFromLogin(): String?{
        return userAuthRepository.getStatusFromLogin()
    }

    fun registerUser(username: String, password: String, confirmPassword: String, name: String, surname: String, email: String, phoneNumber: String, address: String) {
        userAuthRepository.requestToRegister(username, password, confirmPassword, name, surname, email, phoneNumber, address)
    }

    fun loginUser(id:String, password: String, errorCodes:MutableList<Int>) {
        userAuthRepository.requestToLogin(id,password,fun(){
            Log.d("STATUS",getStatusFromLogin().toString())
            if(getStatusFromLogin().toString() == "fail") {
                authListener?.OnFailure(errorCodes)
                Log.d("On Failure","failed")
            }
            else {
                authListener?.OnSuccess()
            }
        })
    }

    fun onLoginButtonClick(view: View) {
        var errorCodes: MutableList<Int> = mutableListOf()
        authListener?.OnStarted()

        if (id.isNullOrEmpty() || password.isNullOrEmpty()) {
            errorCodes.add(820)
        }else {
            if (!HealthIdValidator.isValid(id.toString())) {
                errorCodes.add(910)
            }
        }
        if (errorCodes.size==0){
            loginUser(id.toString(),password.toString(),errorCodes)

        }else{
            authListener?.OnFailure(errorCodes)
        }
    }

    fun onRegisterButtonClick(view: View) {
        var errorCodes = mutableListOf<Int>()

        authListener?.OnStarted()

        if(id.isNullOrEmpty()){
            errorCodes.add(911)
        }else{
            if(!HealthIdValidator.isValid(id.toString())){
                errorCodes.add(910)
                Log.d("HealthId", "Wrong HealthId")
            }else{
                Log.d("HealthId", "healthId is ok")
            }
        }

        if(email.isNullOrEmpty()){
            errorCodes.add(921)
        }else{
            if(!EmailValidator.isValid(email.toString())){
                errorCodes.add(920)
                Log.d("Email", "Wrong Email")
            }else{
                Log.d("Email", "email is ok")
            }
        }

        if(phoneNumber.isNullOrEmpty()){
            errorCodes.add(931)
        }else{
            if(!PhoneNumberValidator.isValid(phoneNumber.toString())){
                errorCodes.add(930)
                Log.d("Phone Number", "Wrong Phone Number")
            }else{
                Log.d("Phone Number", "Phone Number is ok")
            }
        }

        if(firstName.isNullOrEmpty()){
            errorCodes.add(940)
        }

        if(lastName.isNullOrEmpty()){
            errorCodes.add(950)
        }

        if(!PasswordValidator.isValid(password.toString())){
            errorCodes.add(960)
        }

        if(!PasswordConfirmValidator.isValid(password.toString(), passwordConfirm.toString())){
            errorCodes.add(961)
        }

        if(errorCodes.size == 0){
            authListener?.OnSuccess()
            registerUser(id.toString(), password.toString(), passwordConfirm.toString(),
                firstName.toString(), lastName.toString(), email.toString(), phoneNumber.toString())
        }else {
            authListener?.OnFailure(errorCodes)
        }
    }
}