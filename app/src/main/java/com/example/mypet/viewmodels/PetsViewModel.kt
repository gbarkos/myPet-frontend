package com.example.mypet.viewmodels

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.MedicalRecord
import com.example.mypet.models.Pet
import com.example.mypet.models.Vaccination
import com.example.mypet.models.requests.PetPostRequest
import com.example.mypet.models.requests.SetPetAsMissingRequest
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.models.responses.PetsGetResponse
import com.example.mypet.models.responses.PetsLimitedGetResponse
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.PetsRepository
import com.example.mypet.utils.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PetsViewModel : ViewModel() {

    var id : String? = null
    var name : String? = null
    var birthdate : String? = null
    var colour : String? = null
    var distinguishingMarks : String? = null
    var breed : String? = null
    var sex : String? = null
    var photo : String? = null
    var weight : Double? = null
    var height : Double? = null
    var medicalRecord : MedicalRecord? = null
    var _id: String? = null
    var recordId : String? = null
    var isMissing: Boolean = false
    var authListener: AuthFunctions? = null

    private val petsRepository: PetsRepository = PetsRepository


    fun getLoadStateFromRepo(): LiveData<Event<NetworkLoadingState>> {
        return PetsRepository.loadState
    }

    fun getPetsDataFromRepo(): MutableLiveData<PetsLimitedGetResponse> {
        return PetsRepository.getPetsResponse()
    }

    fun getPetDataFromRepo(): MutableLiveData<PetGetResponse> {
        return PetsRepository.getPetResponse()
    }

    fun getPetObj() : Pet? {
        return PetsRepository.getPetResponse().value?.pet
    }

    fun requestPets(){
        petsRepository.requestPets()
    }

    fun requestPet(_id : String){
        petsRepository.requestPet(_id)
    }

    fun getStatusFromUpdate(): String?{
        return PetsRepository.getStatusFromUpdate()
    }

    fun addPet(id : String?, name : String, birthdate : String, colour : String, distinguishingMarks : String?,
               breed : String, sex : String, weight : String?, height : String?){
        petsRepository.addNewPet(id, name, birthdate, colour, distinguishingMarks, breed, sex, weight, height)
    }

    fun updatePet(id : String?,
                  distinguishingMarks : String?,
                  weight : String?,
                  height : String?,
                  photo: String?,
                  _id : String?){
        authListener?.OnStarted()
        doUpdatePet(id, distinguishingMarks, weight, height, photo, _id)
    }

    fun doUpdatePet(id : String?,
                    distinguishingMarks : String?,
                    weight : String?,
                    height : String?,
                    photo: String?,
                    _id : String?){
        viewModelScope.launch{
            petsRepository.updatePet(id, distinguishingMarks, weight, height, photo, _id, fun(){
                if(getStatusFromUpdate().toString() == "fail"){
                    var errors = mutableListOf<Int>()
                    authListener?.OnFailure(errors)
                }else{
                    authListener?.OnSuccess()
                }
            })
        }
    }

    fun setPetAsMissing(lat: String, lng: String, contactInfo: List<String>, petId: String?){
        authListener?.OnStarted()
        viewModelScope.launch {
            petsRepository.setPetAsMissing(lat, lng, contactInfo, petId, fun() {
                Log.d("STATUS",petsRepository.getStatusFromSetPetAsMissing())
                if(petsRepository.getStatusFromSetPetAsMissing() == "fail"){
                    authListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }else{
                    authListener?.OnSuccess()
                }
            })
        }
    }

    fun setPetAsFound(petId: String?){
        authListener?.OnStarted()
        viewModelScope.launch {
            petsRepository.setPetAsFound(petId, fun() {
                Log.d("STATUS",petsRepository.getStatusFromSetPetAsFound())
                if(petsRepository.getStatusFromSetPetAsFound() == "fail"){
                    authListener?.OnFailure(null)
                    Log.d("On Failure","failed")
                }else{
                    authListener?.OnSuccess()
                }
            })
        }
    }
}