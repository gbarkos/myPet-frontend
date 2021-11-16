package com.example.mypet.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.models.responses.PetsGetResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PetsRepository {

    private const val TAG = "PetsRepository"
    val gson = Gson()
    private var statusFromGetPets: String = ""

    private val petsGetResponse : MutableLiveData<PetsGetResponse> = MutableLiveData()
    fun getPetsResponse() : MutableLiveData<PetsGetResponse> {
        return petsGetResponse;
    }

    private val petGetResponse : MutableLiveData<PetGetResponse> = MutableLiveData()
    fun getPetResponse() : MutableLiveData<PetGetResponse> {
        return petGetResponse;
    }

    fun requestPets() {
        val dataSource = ServiceGenerator
        Log.i(TAG, "Pets response: Call is started")
        dataSource.getMyPetApi()
            .getPets()
            .enqueue(object : Callback<PetsGetResponse> {
                override fun onResponse(
                    call: Call<PetsGetResponse>,
                    response: Response<PetsGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(PetsRepository.TAG, "onResponse: Response Successful")
                        petsGetResponse.postValue(response.body())

                    }
                }
                override fun onFailure(call: Call<PetsGetResponse>, t: Throwable) {
                    Log.i(PetsRepository.TAG, "onFailure: " + t.message)
                }

            })
    }

    fun requestPet(_id: String) {
        val dataSource = ServiceGenerator
        Log.i(TAG, "Pet response: Call is started")
        dataSource.getMyPetApi()
            .getPet(_id)
            .enqueue(object : Callback<PetGetResponse> {
                override fun onResponse(
                    call: Call<PetGetResponse>,
                    response: Response<PetGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(PetsRepository.TAG, "onResponse: Response Successful")
                        petGetResponse.postValue(response.body())

                    }
                }
                override fun onFailure(call: Call<PetGetResponse>, t: Throwable) {
                    Log.i(PetsRepository.TAG, "onFailure: " + t.message)
                }
            })
    }
}