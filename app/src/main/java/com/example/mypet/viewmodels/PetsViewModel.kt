package com.example.mypet.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.mypet.models.MedicalRecord
import com.example.mypet.models.Vaccination
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.models.responses.PetsGetResponse
import com.example.mypet.models.responses.PetsLimitedGetResponse
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.PetsRepository
import com.example.mypet.utils.SingleLiveEvent
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

    private val petsRepository: PetsRepository = PetsRepository

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
}