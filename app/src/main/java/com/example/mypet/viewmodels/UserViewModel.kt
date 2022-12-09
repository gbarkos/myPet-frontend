package com.example.mypet.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypet.models.enums.RegisterErrorCodes
import com.example.mypet.models.responses.UserGetResponse
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.UserAuthRepository
import com.example.mypet.utils.ResponseFunctions
import com.example.mypet.utils.*
import com.example.mypet.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {

    var username: String? = null
    var password: String? = null
    var confirmPassword: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var name: String? = null
    var surname: String? = null
    var address: String? = null
    var receiveMails: Boolean? = false
    var responseListener: ResponseFunctions? = null

    private val userAuthRepository: UserAuthRepository = UserAuthRepository


    fun getUserLoginDataFromRepo(): SingleLiveEvent<UserLoginRegisterPostResponse> {
        return userAuthRepository.getUserLoginResponse()
    }

    fun getUserRegisterDataFromRepo(): SingleLiveEvent<UserLoginRegisterPostResponse> {
        return userAuthRepository.getUserRegisterResponse()
    }

    fun getUserUpdateDataFromRepo(): SingleLiveEvent<UserGetResponse> {
        return userAuthRepository.getUserUpdateResponse()
    }

    fun getUserProfileDataFromRepo(): SingleLiveEvent<UserGetResponse>{
        return userAuthRepository.getMyProfileResponse();
    }

    fun getStatusFromLogin(): String?{
        return userAuthRepository.getStatusFromLogin()
    }

    fun getStatusFromRegister(): String?{
        return userAuthRepository.getStatusFromRegister()
    }

    fun getStatusFromUpdate(): String?{
        return userAuthRepository.getStatusFromUpdate()
    }

    private var statusFromLoginValidation : SingleLiveEvent<String> = SingleLiveEvent()
    fun getStatusFromLoginValidation(): SingleLiveEvent<String> {
        return statusFromLoginValidation
    }

    private var statusFromRegisterValidation : MutableLiveData<List<RegisterErrorCodes>> = MutableLiveData()
    fun getStatusFromRegisterValidation() : MutableLiveData<List<RegisterErrorCodes>>{
        return statusFromRegisterValidation
    }

    fun registerUser(username: String, password: String, confirmPassword: String, name: String, surname: String, email: String, phoneNumber: String, address: String) {
        viewModelScope.launch {
            userAuthRepository.requestToRegister(username, password, confirmPassword, name, surname, email, phoneNumber, address, fun (){
                Log.d("STATUS",getStatusFromRegister().toString())
                if(getStatusFromRegister().toString() == "fail") {
                    responseListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }
                else {
                    responseListener?.OnSuccess()
                }
            })
        }

    }

    fun getProfile(){
        viewModelScope.launch {
            userAuthRepository.requestUserInfo()
        }
    }

    fun loginUser(username:String, password: String) {
        viewModelScope.launch {
            userAuthRepository.requestToLogin(username, password, fun(){
                Log.d("STATUS",getStatusFromLogin().toString())
                if(getStatusFromLogin().toString() == "fail") {
                    responseListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }
                else {
                    responseListener?.OnSuccess()
                }
            })
        }
    }

    fun updateUserEmailPreferences(shouldReceiveEmail : Boolean){
        responseListener?.OnStarted()
        viewModelScope.launch {
            userAuthRepository.requestEmailPreferencesChange(shouldReceiveEmail, fun() {
                Log.d("STATUS",getStatusFromUpdate().toString())
                if(getStatusFromUpdate().toString() == "fail"){
                    responseListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }else{
                    responseListener?.OnSuccess()
                }
            })
        }
    }

    fun onLoginButtonClick(view: View) {
        if (username.isNullOrEmpty()){
            return statusFromLoginValidation.postValue("Παρακαλώ εισάγετε όνομα χρήστη")
        }

        if(password.isNullOrEmpty()) {
            return statusFromLoginValidation.postValue("Παρακαλώ εισάγετε κωδικό πρόσβασης")
        }

        responseListener?.OnStarted()
        loginUser(password.toString(), password.toString())
    }

    fun onRegisterButtonClick(view: View) {
        var errorCodes = mutableListOf<RegisterErrorCodes>()
        statusFromRegisterValidation.value = errorCodes

        if(username.isNullOrEmpty()){
            errorCodes.add(RegisterErrorCodes.MandatoryUsername)
        }
        if(email.isNullOrEmpty()){
            errorCodes.add(RegisterErrorCodes.MandatoryEmail)
        }
        if(!EmailValidator.isValid(email.toString())){
            errorCodes.add(RegisterErrorCodes.InvalidEmail)
        }
        if(password.isNullOrEmpty()){
            errorCodes.add(RegisterErrorCodes.MandatoryPassword)
        }
        if(confirmPassword.isNullOrEmpty()){
            errorCodes.add(RegisterErrorCodes.MandatoryConfirmPassword)
        }
        if(!PasswordValidator.isValid(password.toString())){
            errorCodes.add(RegisterErrorCodes.InvalidPassword)
        }
        if(!PasswordConfirmValidator.isValid(password.toString(), confirmPassword.toString())){
            errorCodes.add(RegisterErrorCodes.PasswordsDoNotMatch)
        }

        if(errorCodes.size == 0){
            responseListener?.OnStarted()
            registerUser(username.toString(), password.toString(), confirmPassword.toString(),
                "", "", email.toString(), "", "")
        }else {
            statusFromRegisterValidation.postValue(errorCodes)
        }
    }
}