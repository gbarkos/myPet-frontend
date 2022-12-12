package com.example.mypet.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypet.models.forms.RegisterValidationForm
import com.example.mypet.models.enums.RegisterErrorCodes
import com.example.mypet.models.enums.RegisterFormFields
import com.example.mypet.models.responses.UserGetResponse
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.UserAuthRepository
import com.example.mypet.utils.*
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

    private var registerFormValidation = RegisterValidationForm()
    private var observedRegisterFormValidation : MutableLiveData<RegisterValidationForm> = MutableLiveData()
    fun getRegisterFormValidation() : MutableLiveData<RegisterValidationForm>{
        return observedRegisterFormValidation
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
            userAuthRepository.requestToLogin(username, password, fun(msg : String){
                Log.d("STATUS",getStatusFromLogin().toString())
                if(getStatusFromLogin().toString() == "fail") {
                    responseListener?.OnFailure(msg)
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

    fun saveFcmToken(token : String){
        responseListener?.OnStarted()
        viewModelScope.launch {
            userAuthRepository.saveFcmToken(token, fun() {
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
        loginUser(username.toString(), password.toString())
    }

    fun onRegisterButtonClick(username : String, email : String, password: String, confirmPassword: String) {
        validateRegisterFormField(RegisterFormFields.Username, username)
        validateRegisterFormField(RegisterFormFields.Email, email)
        validateRegisterFormField(RegisterFormFields.Password, password)
        validateRegisterFormField(RegisterFormFields.ConfirmPassword, confirmPassword)

        if(registerFormValidation.isFormValidated()){
            responseListener?.OnStarted()
            registerUser(username, password, confirmPassword,
                "", "", email, "", "")
        }
    }

    fun validateRegisterFormField(field : RegisterFormFields, value : String){
        when(field){
            RegisterFormFields.Username -> {
                if(value.isNullOrEmpty()){
                    registerFormValidation.usernameError = RegisterErrorCodes.MissingUsername
                    registerFormValidation.hasUsernamePassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                if(!UsernameValidator.isValid(value)){
                    registerFormValidation.usernameError = RegisterErrorCodes.InvalidUsername
                    registerFormValidation.hasUsernamePassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                registerFormValidation.usernameError = RegisterErrorCodes.NoError
                registerFormValidation.hasUsernamePassedValidation = true
                observedRegisterFormValidation.postValue(registerFormValidation)
                return
            }
            RegisterFormFields.Email -> {
                if(value.isNullOrEmpty()){
                    registerFormValidation.emailError = RegisterErrorCodes.MissingEmail
                    registerFormValidation.hasEmailPassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                if(!EmailValidator.isValid(value)){
                    registerFormValidation.emailError = RegisterErrorCodes.InvalidEmail
                    registerFormValidation.hasEmailPassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                registerFormValidation.emailError = RegisterErrorCodes.NoError
                registerFormValidation.hasEmailPassedValidation = true
                observedRegisterFormValidation.postValue(registerFormValidation)
                return
            }
            RegisterFormFields.Password -> {
                if(value.isNullOrEmpty()){
                    registerFormValidation.passwordError = RegisterErrorCodes.MissingPassword
                    registerFormValidation.hasPasswordPassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                if(!PasswordValidator.isValid(value)){
                    registerFormValidation.passwordError = RegisterErrorCodes.InvalidPassword
                    registerFormValidation.hasPasswordPassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                if(!PasswordConfirmValidator.isValid(value, registerFormValidation.confirmPasswordValue)){
                    registerFormValidation.passwordError = RegisterErrorCodes.PasswordsDoNotMatch
                    registerFormValidation.hasPasswordPassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                registerFormValidation.passwordError = RegisterErrorCodes.NoError
                registerFormValidation.hasPasswordPassedValidation = true
                registerFormValidation.passwordValue = value
                observedRegisterFormValidation.postValue(registerFormValidation)
                return

            }
            RegisterFormFields.ConfirmPassword -> {
                if(value.isNullOrEmpty()){
                    registerFormValidation.confirmPasswordError = RegisterErrorCodes.MissingConfirmPassword
                    registerFormValidation.hasConfirmPasswordPassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                if(!PasswordConfirmValidator.isValid(registerFormValidation.passwordValue, value)){
                    registerFormValidation.confirmPasswordError = RegisterErrorCodes.PasswordsDoNotMatch
                    registerFormValidation.confirmPasswordValue = value
                    registerFormValidation.hasConfirmPasswordPassedValidation = false
                    observedRegisterFormValidation.postValue(registerFormValidation)
                    return
                }
                registerFormValidation.confirmPasswordError = RegisterErrorCodes.NoError
                registerFormValidation.hasConfirmPasswordPassedValidation = true
                registerFormValidation.confirmPasswordValue = value
                observedRegisterFormValidation.postValue(registerFormValidation)
                return
            }
        }
    }
}