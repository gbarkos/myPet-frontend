package com.example.mypet.viewmodels

import android.util.Log
import android.view.View
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
import com.example.mypet.utils.*
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
    var authListener: AuthFunctions? = null

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

    fun onRegisterButtonClick(view: View) {
        var errorCodes = mutableListOf<Int>()

        authListener?.OnStarted()

        /*if(username.isNullOrEmpty()){
            errorCodes.add(911)
        }else{
            if(!UsernameValidator.isValid(username.toString())){
                errorCodes.add(910)
                Log.d("HealthId", "Wrong HealthId")
            }else{
                Log.d("HealthId", "healthId is ok")
            }
        }

        if(email.isNullOrEmpty()){
            errorCodes.add(921)
        }else{
            if(!EmailValidator.isValid(email.toString())){
                errorCodes.add(920)
                Log.d("Email", "Wrong Email")
            }else{
                Log.d("Email", "email is ok")
            }
        }

        if(phoneNumber.isNullOrEmpty()){
            errorCodes.add(931)
        }else{
            if(!PhoneNumberValidator.isValid(phoneNumber.toString())){
                errorCodes.add(930)
                Log.d("Phone Number", "Wrong Phone Number")
            }else{
                Log.d("Phone Number", "Phone Number is ok")
            }
        }

        if(address.isNullOrEmpty()){
            errorCodes.add(970)
        }

        if(name.isNullOrEmpty()){
            errorCodes.add(940)
        }

        if(name.isNullOrEmpty()){
            errorCodes.add(950)
        }

        if(!PasswordValidator.isValid(password.toString())){
            errorCodes.add(960)
        }

        if(!PasswordConfirmValidator.isValid(password.toString(), confirmPassword.toString())){
            errorCodes.add(961)
        }

        if(errorCodes.size == 0){
            authListener?.OnSuccess()
            registerUser(username.toString(), password.toString(), confirmPassword.toString(),
                name.toString(), surname.toString(), email.toString(), phoneNumber.toString(), address.toString())
        }else {
            authListener?.OnFailure(errorCodes)
        }*/
    }
}