package com.example.mypet.viewmodels

import android.util.Log
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.UserAuthRepository
import com.example.mypet.utils.SingleLiveEvent

class UserAuthViewModel {
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
        userAuthRepository.requestToRegister(username, password, confirmPassword, name, surname, email, phoneNumber)
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
}