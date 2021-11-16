package com.example.mypet.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypet.models.MedicalRecord
import com.example.mypet.models.responses.PetGetResponse
import com.example.mypet.models.responses.PetsGetResponse
import com.example.mypet.models.responses.UserLoginRegisterPostResponse
import com.example.mypet.repositories.PetsRepository
import com.example.mypet.utils.SingleLiveEvent
import java.util.*

class PetsViewModel : ViewModel() {

    val id : String? = null
    val name : String? = null
    val birthdate : Date? = null
    val colour : String? = null
    val distinguishingMarks : String? = null
    val breed : String? = null
    val sex : String? = null
    val photo : String? = null
    val weight : Double? = null
    val height : Double? = null
    val medicalRecord : MedicalRecord? = null
    val _id: String? = null

    private val petsRepository: PetsRepository = PetsRepository

    fun getPetsDataFromRepo(): MutableLiveData<PetsGetResponse> {
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