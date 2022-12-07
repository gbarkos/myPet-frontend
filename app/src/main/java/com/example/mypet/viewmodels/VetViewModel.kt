package com.example.mypet.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.models.responses.VetGetResponse
import com.example.mypet.models.responses.VetLoginRegisterPostResponse
import com.example.mypet.repositories.UserAuthRepository
import com.example.mypet.repositories.VetRepository
import com.example.mypet.utils.*
import kotlinx.coroutines.launch

class VetViewModel: ViewModel() {

    var username: String? = null
    var password: String? = null
    var confirmPassword: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var name: String? = null
    var surname: String? = null
    var address: String? = null
    var authListener: AuthFunctions? = null

    private val userAuthRepository: UserAuthRepository = UserAuthRepository


    fun getVetLoginDataFromRepo(): SingleLiveEvent<VetLoginRegisterPostResponse> {
        return VetRepository.getVetLoginResponse()
    }

    fun getVetProfileDataFromRepo(): SingleLiveEvent<VetGetResponse>{
        return VetRepository.getVetMyProfileResponse()
    }

    fun getFailureMessageFromRegister(): SingleLiveEvent<String> {
        return userAuthRepository.getFailureMessageFromRegister()
    }

    fun getStatusFromLogin(): String?{
        return VetRepository.getStatusFromLogin()
    }

    fun getStatusFromGetProfile(): String? {
        return VetRepository.getStatusFromGetProfile()
    }

    fun loginVet(username:String, password: String, errorCodes:MutableList<Int>) {
        viewModelScope.launch {
            VetRepository.requestToLogin(username, password, fun(){
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
            loginVet(username.toString(),password.toString(),errorCodes)

        }else{
            authListener?.OnFailure(errorCodes)
        }
    }

    fun getVetProfile(){
        authListener?.OnStarted()
        viewModelScope.launch{
            VetRepository.getVetProfile(fun(){
                if(getStatusFromGetProfile().toString() == "fail"){
                    authListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }else{
                    authListener?.OnSuccess()
                }
            })
        }
    }
}