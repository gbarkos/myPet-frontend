package com.example.mypet.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.mypet.api.ServiceGenerator
import com.example.mypet.models.MedicalRecord
import com.example.mypet.models.Vaccination
import com.example.mypet.models.requests.PetPostRequest
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.models.responses.PetsGetResponse
import com.example.mypet.models.responses.PetsLimitedGetResponse
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.PetsRepository
import com.example.mypet.utils.*
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

    fun requestPets(){
        petsRepository.requestPets()
    }

    fun requestPet(_id : String){
        petsRepository.requestPet(_id)
    }

    fun addPet(id : String?, name : String, birthdate : String, colour : String, distinguishingMarks : String?,
               breed : String, sex : String, weight : String?, height : String?){
        petsRepository.addNewPet(id, name, birthdate, colour, distinguishingMarks, breed, sex, weight, height)
    }
}