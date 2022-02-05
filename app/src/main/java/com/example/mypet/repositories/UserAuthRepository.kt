package com.example.mypet.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.ErrorResponse
import com.example.mypet.models.requests.UserLoginPostRequest
import com.example.mypet.models.requests.UserRegisterPostRequest
import com.example.mypet.models.responses.UserGetResponse
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.utils.SingleLiveEvent
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserAuthRepository {
    private const val TAG = "AuthRepository"
    val gson = Gson()
    private var statusFromLogin: String = ""

    private val userLoginResponse : SingleLiveEvent<UserLoginRegisterPostResponse> = SingleLiveEvent();
    fun getUserLoginResponse() : SingleLiveEvent<UserLoginRegisterPostResponse> {
        return userLoginResponse;
    }

    private val userRegisterResponse: SingleLiveEvent<UserLoginRegisterPostResponse> = SingleLiveEvent();
    fun getUserRegisterResponse() : SingleLiveEvent<UserLoginRegisterPostResponse> {
        return userRegisterResponse;
    }

    private val userMyProfileResponse: MutableLiveData<UserGetResponse> = MutableLiveData()
    private val failureMessageFromRegister: SingleLiveEvent<String> = SingleLiveEvent()

    fun getFailureMessageFromRegister(): SingleLiveEvent<String>{
        return failureMessageFromRegister
    }
    fun getStatusFromLogin(): String{
        return statusFromLogin
    }

    fun requestToRegister(username: String, password: String, confirmPassword: String, name: String, surname: String, email: String, phoneNumber: String, address: String) {
        val dataSource = ServiceGenerator

        Log.i(TAG, "registerUser: Call is started")
        val body = UserRegisterPostRequest(username, name, surname, email, phoneNumber, address, password, confirmPassword)
        dataSource.getMyPetApi()
            .registerUser(body)
            .enqueue(object : Callback<UserLoginRegisterPostResponse> {
                override fun onResponse(
                    call: Call<UserLoginRegisterPostResponse>,
                    response: Response<UserLoginRegisterPostResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        userRegisterResponse.postValue(response.body())
                    } else {
                        try {
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                            failureMessageFromRegister.postValue(responseObj.message)
                        } catch (e: Exception) {
                            failureMessageFromRegister.postValue("Something Went Wrong")
                        }
                    }
                }
                override fun onFailure(call: Call<UserLoginRegisterPostResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                }
            })
    }

     fun requestToLogin(username: String,password: String, callback: () ->Unit) {
        val dataSource = ServiceGenerator

        Log.i(TAG, "Login user: Call started")
        val body = UserLoginPostRequest(username, password)
        dataSource.getMyPetApi()
            .loginUser(body)
            .enqueue(object : Callback<UserLoginRegisterPostResponse> {
                override fun onResponse(
                    call: Call<UserLoginRegisterPostResponse>,
                    response: Response<UserLoginRegisterPostResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        userLoginResponse.postValue(response.body())
                        statusFromLogin=""
                        callback()
                    } else {
                        try {
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                            statusFromLogin = responseObj.status
                            Log.d("FAILURE MESSAGE", statusFromLogin.toString())
                            callback()
                        } catch (e: Exception) {
                            statusFromLogin = "Something Went Wrong"
                            Log.d("IN CATCH", statusFromLogin.toString())
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<UserLoginRegisterPostResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                    callback()
                }
            })
    }

    fun requestUserInfo() {
        val dataSource = ServiceGenerator

        Log.i(TAG, "My Profile response: Call is started")
        dataSource.getMyPetApi()
            .getUserInfo()
            .enqueue(object : Callback<UserGetResponse> {
                override fun onResponse(
                    call: Call<UserGetResponse>,
                    response: Response<UserGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        userMyProfileResponse.postValue(response.body())
                    }
                }
                override fun onFailure(call: Call<UserGetResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                }
            })
    }
}