package com.example.mypet.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.mypet.R
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.ErrorResponse
import com.example.mypet.models.requests.PetPostRequest
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.models.responses.PetsLimitedGetResponse
import com.example.mypet.utils.Event
import com.example.mypet.utils.NetworkLoadingState
import com.example.mypet.utils.SingleLiveEvent
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

object PetsRepository {

    private const val TAG = "PetsRepository"
    val gson = Gson()
    private var statusFromGetPets: String = ""
    private val failureMessageFromNewPet: SingleLiveEvent<String> = SingleLiveEvent()

    private var _loadState = MutableLiveData<Event<NetworkLoadingState>>()
    val loadState: LiveData<Event<NetworkLoadingState>>
        get() = _loadState

    val petsGetResponse : MutableLiveData<PetsLimitedGetResponse> = MutableLiveData()
    fun getPetsResponse() : MutableLiveData<PetsLimitedGetResponse> {
        return petsGetResponse;
    }

    val petGetResponse : MutableLiveData<PetGetResponse> = MutableLiveData()
    fun getPetResponse() : MutableLiveData<PetGetResponse> {
        return petGetResponse;
    }

    fun requestPets() {
        val dataSource = ServiceGenerator
        Log.i(TAG, "Pets response: Call is started")
        dataSource.getMyPetApi()
            .getPets()
            .enqueue(object : Callback<PetsLimitedGetResponse> {
                override fun onResponse(
                    call: Call<PetsLimitedGetResponse>,
                    response: Response<PetsLimitedGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.i(PetsRepository.TAG, "onResponse: Response Successful")
                        petsGetResponse.postValue(response.body())
                        _loadState.value = Event(NetworkLoadingState.OnSuccess)
                    }
                }
                override fun onFailure(call: Call<PetsLimitedGetResponse>, t: Throwable) {
                    Log.i(PetsRepository.TAG, "onFailure: " + t.message)
                    _loadState.value = Event(NetworkLoadingState.OnError(t.message.toString()))
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
                        _loadState.value = Event(NetworkLoadingState.OnSuccess)
                    }
                }
                override fun onFailure(call: Call<PetGetResponse>, t: Throwable) {
                    Log.i(PetsRepository.TAG, "onFailure: " + t.message)
                    _loadState.value = Event(NetworkLoadingState.OnError(t.message.toString()))
                }
            })
    }

    fun addNewPet(id : String?,
                  name : String,
                  birthdate : String,
                  colour : String,
                  distinguishingMarks : String?,
                  breed : String,
                  sex : String,
                  weight : String?,
                  height : String?) {
        val dataSource = ServiceGenerator
        //Log.i(PetsRepository.TAG, "Pet response: Call started")
        dataSource.getMyPetApi()
            .newPet(PetPostRequest(id, name, birthdate, colour, distinguishingMarks, breed, sex, weight, height ))
            .enqueue(object : Callback<PetGetResponse> {
                override fun onResponse(
                    call: Call<PetGetResponse>,
                    response: Response<PetGetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        //Log.i(petsRepository.TAG, "onResponse: Response Successful")
                        petGetResponse.postValue(response.body())
                        _loadState.value = Event(NetworkLoadingState.OnSuccess)
                    }
                }
                override fun onFailure(call: Call<PetGetResponse>, t: Throwable) {
                    //Log.i(PetsRepository.TAG, "onFailure: " + t.message)
                    _loadState.value = Event(NetworkLoadingState.OnError(t.message.toString()))
                }
            })
    }
}