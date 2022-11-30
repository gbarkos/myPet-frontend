package com.example.mypet.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.UserAuthRepository
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.utils.*
import com.example.mypet.utils.SingleLiveEvent
import kotlinx.coroutines.launch

//ERROR CODES
//810 -> Username must be at least 6 chars long(Login)
//811 -> Username must not be empty(Login)

//820 -> Fill all fields

//910 -> Username must be at least 6 chars long
//911 -> Username must not be empty

//920 -> email address is not valid
//921 -> email address must not be empty

//930 -> phone number is not valid
//931 -> phone number must not  be empty

//940 -> First name must not be empty

//950 -> Last name must not be empty

//960 -> Password must be at least 8 chars
//961 -> Passwords do not match

//970 -> Address must not be empty

class UserAuthViewModel: ViewModel() {

    var username: String? = null
    var password: String? = null
    var confirmPassword: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var name: String? = null
    var surname: String? = null
    var address: String? = null
    var receiveMails: Boolean? = false
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

    fun getProfile(){
        viewModelScope.launch {
            userAuthRepository.requestUserInfo()

        }
    }

    fun loginUser(username:String, password: String, errorCodes:MutableList<Int>) {
        viewModelScope.launch {
            userAuthRepository.requestToLogin(username, password, fun(){
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
    }

    fun onLoginButtonClick(view: View) {
        var errorCodes: MutableList<Int> = mutableListOf()
        authListener?.OnStarted()

        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            errorCodes.add(820)
        }else {
            if (!UsernameValidator.isValid(username.toString())) {
                errorCodes.add(910)
            }
        }
        if (errorCodes.size==0){
            loginUser(username.toString(),password.toString(),errorCodes)

        }else{
            authListener?.OnFailure(errorCodes)
        }
    }

    fun onRegisterButtonClick(view: View) {
        var errorCodes = mutableListOf<Int>()

        authListener?.OnStarted()

        if(username.isNullOrEmpty()){
            errorCodes.add(911)
        }else{
            if(!UsernameValidator.isValid(username.toString())){
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

       /* if(phoneNumber.isNullOrEmpty()){
            errorCodes.add(931)
        }else{
            if(!PhoneNumberValidator.isValid(phoneNumber.toString())){
                errorCodes.add(930)
                Log.d("Phone Number", "Wrong Phone Number")
            }else{
                Log.d("Phone Number", "Phone Number is ok")
            }
        }

        if(address.isNullOrEmpty()){
            errorCodes.add(970)
        }

        if(name.isNullOrEmpty()){
            errorCodes.add(940)
        }

        if(name.isNullOrEmpty()){
            errorCodes.add(950)
        }*/

        if(!PasswordValidator.isValid(password.toString())){
            errorCodes.add(960)
        }

        if(!PasswordConfirmValidator.isValid(password.toString(), confirmPassword.toString())){
            errorCodes.add(961)
        }

        if(errorCodes.size == 0){
            authListener?.OnSuccess()
            registerUser(username.toString(), password.toString(), confirmPassword.toString(),
                "", "", email.toString(), "", "")
        }else {
            authListener?.OnFailure(errorCodes)
        }
    }
}