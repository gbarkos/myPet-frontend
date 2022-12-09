package com.example.mypet.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.ErrorResponse
import com.example.mypet.models.requests.UserChangeEmailPreferencesRequest
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
    private var statusFromRegister: String = ""

    private var statusFromUpdate: String = ""
    private val updateUserResponse : SingleLiveEvent<UserGetResponse> = SingleLiveEvent();
    fun getUserUpdateResponse() : SingleLiveEvent<UserGetResponse> {
        return updateUserResponse;
    }
    fun getStatusFromUpdate(): String{
        return statusFromUpdate
    }

    private val userLoginResponse : SingleLiveEvent<UserLoginRegisterPostResponse> = SingleLiveEvent();
    fun getUserLoginResponse() : SingleLiveEvent<UserLoginRegisterPostResponse> {
        return userLoginResponse;
    }

    private val userRegisterResponse: SingleLiveEvent<UserLoginRegisterPostResponse> = SingleLiveEvent();
    fun getUserRegisterResponse() : SingleLiveEvent<UserLoginRegisterPostResponse> {
        return userRegisterResponse;
    }

    private val userMyProfileResponse: SingleLiveEvent<UserGetResponse> = SingleLiveEvent()
    fun getMyProfileResponse() : SingleLiveEvent<UserGetResponse> {
        return userMyProfileResponse;
    }

    fun getStatusFromRegister(): String{
        return statusFromRegister
    }

    fun getStatusFromLogin(): String{
        return statusFromLogin
    }

    fun requestToRegister(username: String, password: String, confirmPassword: String, name: String,
                          surname: String, email: String, phoneNumber: String, address: String, callback : () -> Unit) {

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
                        statusFromRegister = ""
                        callback()
                    } else {
                        try {
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                            statusFromRegister = responseObj.status
                            callback()
                        } catch (e: Exception) {
                            statusFromRegister = "Κάτι πήγε λάθος"
                            callback()
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

    fun requestEmailPreferencesChange(shouldReceiveMails : Boolean, callback: () ->Unit) {
        val dataSource = ServiceGenerator
        val body = UserChangeEmailPreferencesRequest(shouldReceiveMails)

        dataSource.getMyPetApi()
            .changeEmailStatus(body)
            .enqueue(object : Callback<UserGetResponse> {
                override fun onResponse(
                    call: Call<UserGetResponse>,
                    response: Response<UserGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        updateUserResponse.postValue(response.body())
                        statusFromUpdate=""
                        callback()
                    } else {
                        try {
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                            statusFromUpdate = responseObj.status
                            Log.d("FAILURE MESSAGE", statusFromUpdate.toString())
                            callback()
                        } catch (e: Exception) {
                            statusFromUpdate = "Something Went Wrong"
                            Log.d("IN CATCH", statusFromUpdate.toString())
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<UserGetResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                    callback()
                }
            })
    }
}