package com.example.mypet.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.ErrorResponse
import com.example.mypet.models.Vet
import com.example.mypet.models.requests.*
import com.example.mypet.models.responses.MedicalRecordPatchResponse
import com.example.mypet.utils.Event
import com.example.mypet.utils.NetworkLoadingState
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MedicalRecordRepository {
    private const val TAG = "MedicalRecordRepository"
    val gson = Gson()

    private var _loadState = MutableLiveData<Event<NetworkLoadingState>>()
    val loadState: LiveData<Event<NetworkLoadingState>>
        get() = _loadState

    private var statusFromUpdateRecord : String = ""
    fun getStatusFromUpdateRecord(): String{
        return statusFromUpdateRecord
    }

    val medicalRecordUpdateResponse : MutableLiveData<MedicalRecordPatchResponse> = MutableLiveData()
    fun getMedicalRecordResponse() : MutableLiveData<MedicalRecordPatchResponse> {
        return medicalRecordUpdateResponse
    }

    fun addVaccination(medicalRecordId : String?, batchNumber : String?, manufacturer : String, name : String, expirationDate : String?,
        vaccinationDate : String, validUntil : String, vet : Vet? = null, callback: () -> Unit) {
        val dataSource = ServiceGenerator
        dataSource.getMyPetApi()
            .addVaccination(VaccinationPostRequest(batchNumber, manufacturer, name, expirationDate, vaccinationDate, validUntil, vet), medicalRecordId)
            .enqueue(object : Callback<MedicalRecordPatchResponse> {
                override fun onResponse(
                    call : Call<MedicalRecordPatchResponse>,
                    response: Response<MedicalRecordPatchResponse>
                ){
                    if (response.isSuccessful && response.body() != null){
                        medicalRecordUpdateResponse.postValue(response.body())
                        //_loadState.value = Event(NetworkLoadingState.OnSuccess)
                        statusFromUpdateRecord=""
                        callback()
                    }else{
                        try{
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.toString(),
                                ErrorResponse::class.java
                            )
                            statusFromUpdateRecord = responseObj.status
                            callback()
                        }catch(e: Exception){
                            statusFromUpdateRecord = "Something went wrong"
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<MedicalRecordPatchResponse>, t: Throwable){
                    //_loadState.value = Event(NetworkLoadingState.OnError(t.message.toString()))
                    callback()
                }
            })
    }



    fun addVermifugation(medicalRecordId : String?, manufacturer : String, name : String, expirationDate : String?,
                       vermifugationDate : String, validUntil : String, vet : Vet? = null, callback: () -> Unit) {
        val dataSource = ServiceGenerator
        dataSource.getMyPetApi()
            .addVermifugation(VermifugationPatchRequest(manufacturer, name, expirationDate, vermifugationDate, validUntil, vet), medicalRecordId)
            .enqueue(object : Callback<MedicalRecordPatchResponse> {
                override fun onResponse(
                    call : Call<MedicalRecordPatchResponse>,
                    response: Response<MedicalRecordPatchResponse>
                ){
                    if (response.isSuccessful && response.body() != null){
                        medicalRecordUpdateResponse.postValue(response.body())
                        //_loadState.value = Event(NetworkLoadingState.OnSuccess)
                        statusFromUpdateRecord=""
                        callback()
                    }else{
                        try{
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.toString(),
                                ErrorResponse::class.java
                            )
                            statusFromUpdateRecord = responseObj.status
                            callback()
                        }catch(e: Exception){
                            statusFromUpdateRecord = "Something went wrong"
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<MedicalRecordPatchResponse>, t: Throwable){
                    callback()
                }
            })
    }

    fun addTreatment(medicalRecordId : String?, medicine : String, disease : String, startOfTreatment : String,
                         endOfTreatment : String, frequency : String, vet : Vet? = null, callback: () -> Unit) {
        val dataSource = ServiceGenerator
        dataSource.getMyPetApi()
            .addTreatment(TreatmentPatchRequest(medicine, disease, startOfTreatment, endOfTreatment, frequency, vet), medicalRecordId)
            .enqueue(object : Callback<MedicalRecordPatchResponse> {
                override fun onResponse(
                    call : Call<MedicalRecordPatchResponse>,
                    response: Response<MedicalRecordPatchResponse>
                ){
                    if (response.isSuccessful && response.body() != null){
                        medicalRecordUpdateResponse.postValue(response.body())
                        //_loadState.value = Event(NetworkLoadingState.OnSuccess)
                        statusFromUpdateRecord=""
                        callback()
                    }else{
                        try{
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.toString(),
                                ErrorResponse::class.java
                            )
                            statusFromUpdateRecord = responseObj.status
                            callback()
                        }catch(e: Exception){
                            statusFromUpdateRecord = "Something went wrong"
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<MedicalRecordPatchResponse>, t: Throwable){
                   callback()
                }
            })
    }

    fun addSurgery(medicalRecordId : String?, name : String, date : String, vet : Vet? = null, callback: () -> Unit) {
        val dataSource = ServiceGenerator
        dataSource.getMyPetApi()
            .addSurgery(SurgeryPatchRequest(name, date, vet), medicalRecordId)
            .enqueue(object : Callback<MedicalRecordPatchResponse> {
                override fun onResponse(
                    call : Call<MedicalRecordPatchResponse>,
                    response: Response<MedicalRecordPatchResponse>
                ){
                    if (response.isSuccessful && response.body() != null){
                        medicalRecordUpdateResponse.postValue(response.body())
                        //_loadState.value = Event(NetworkLoadingState.OnSuccess)
                        statusFromUpdateRecord=""
                        callback()
                    }else{
                        try{
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.toString(),
                                ErrorResponse::class.java
                            )
                            statusFromUpdateRecord = responseObj.status
                            callback()
                        }catch(e: Exception){
                            statusFromUpdateRecord = "Something went wrong"
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<MedicalRecordPatchResponse>, t: Throwable){
                    callback()
                }
            })
    }

    fun addDiagnosticTest(medicalRecordId : String?, name : String, date : String, result: String,
                          vet : Vet? = null, callback : () -> Unit) {
        val dataSource = ServiceGenerator
        dataSource.getMyPetApi()
            .addDiagnosticTest(DiagnosticTestPatchRequest(name, date, result, vet), medicalRecordId)
            .enqueue(object : Callback<MedicalRecordPatchResponse> {
                override fun onResponse(
                    call : Call<MedicalRecordPatchResponse>,
                    response: Response<MedicalRecordPatchResponse>
                ){
                    if (response.isSuccessful && response.body() != null){
                        medicalRecordUpdateResponse.postValue(response.body())
                        //_loadState.value = Event(NetworkLoadingState.OnSuccess)
                        statusFromUpdateRecord=""
                        callback()
                    }else{
                        try{
                            val responseObj: ErrorResponse = gson.fromJson(
                                response.errorBody()?.toString(),
                                ErrorResponse::class.java
                            )
                            statusFromUpdateRecord = responseObj.status
                            callback()
                        }catch(e: Exception){
                            statusFromUpdateRecord = "Something went wrong"
                            callback()
                        }
                    }
                }
                override fun onFailure(call: Call<MedicalRecordPatchResponse>, t: Throwable){
                   callback()
                }
            })
    }

}