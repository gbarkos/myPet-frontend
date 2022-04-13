package com.example.mypet.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.ErrorResponse
import com.example.mypet.models.requests.UserLoginPostRequest
import com.example.mypet.models.requests.UserRegisterPostRequest
import com.example.mypet.models.requests.VetLoginPostRequest
import com.example.mypet.models.responses.UserGetResponse
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.models.responses.VetGetResponse
import com.example.mypet.models.responses.VetLoginRegisterPostResponse
import com.example.mypet.utils.SingleLiveEvent
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VetRepository {
    private const val TAG = "VetRepository"
    val gson = Gson()
    private var statusFromLogin: String = ""

    private val vetLoginResponse : SingleLiveEvent<VetLoginRegisterPostResponse> = SingleLiveEvent();
    fun getVetLoginResponse() : SingleLiveEvent<VetLoginRegisterPostResponse> {
        return vetLoginResponse;
    }

    private val vetMyProfileResponse: MutableLiveData<VetGetResponse> = MutableLiveData()
    private val failureMessageFromRegister: SingleLiveEvent<String> = SingleLiveEvent()

    fun getStatusFromLogin(): String{
        return statusFromLogin
    }

    fun requestToLogin(username: String,password: String, callback: () ->Unit) {
        val dataSource = ServiceGenerator

        Log.i(TAG, "Login vet: Call started")
        val body = VetLoginPostRequest(username, password)
        dataSource.getMyPetApi()
            .loginVet(body)
            .enqueue(object : Callback<VetLoginRegisterPostResponse> {
                override fun onResponse(
                    call: Call<VetLoginRegisterPostResponse>,
                    response: Response<VetLoginRegisterPostResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        vetLoginResponse.postValue(response.body())
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
                override fun onFailure(call: Call<VetLoginRegisterPostResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                    callback()
                }
            })
    }

    fun requestVetInfo() {
        val dataSource = ServiceGenerator

        Log.i(TAG, "My Profile response: Call is started")
        dataSource.getMyPetApi()
            .getVetInfo()
            .enqueue(object : Callback<VetGetResponse> {
                override fun onResponse(
                    call: Call<VetGetResponse>,
                    response: Response<VetGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(TAG, "onResponse: Response Successful")
                        vetMyProfileResponse.postValue(response.body())
                    }
                }
                override fun onFailure(call: Call<VetGetResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: " + t.message)
                }
            })
    }
}