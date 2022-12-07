package com.example.mypet.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypet.models.*
import com.example.mypet.models.responses.MedicalRecordPatchResponse
import com.example.mypet.repositories.MedicalRecordRepository
import com.example.mypet.repositories.PetsRepository
import com.example.mypet.utils.AuthFunctions
import com.example.mypet.utils.Event
import com.example.mypet.utils.NetworkLoadingState
import kotlinx.coroutines.launch

class MedicalRecordViewModel : ViewModel() {
    var _id : String? = null
    lateinit var vaccinations : List<Vaccination>
    lateinit var vermifugations : List<Vermifugation>
    lateinit var treatments : List<Treatment>
    lateinit var surgeries : List<Surgery>
    lateinit var diagnosticTests : List<DiagnosticTest>
    var authListener: AuthFunctions? = null

    private val medicalRecordRepository : MedicalRecordRepository = MedicalRecordRepository

    fun getLoadStateFromRepo(): LiveData<Event<NetworkLoadingState>> {
        return medicalRecordRepository.loadState
    }

    fun getMedicalRecordFromRepo(): MutableLiveData<MedicalRecordPatchResponse> {
        return medicalRecordRepository.getMedicalRecordResponse()
    }

    fun getStatusFromUpdateRecord() {
        medicalRecordRepository.getStatusFromUpdateRecord()
    }

    /*fun addVaccination (medicalRecordId : String?, batchNumber : String?, manufacturer : String, name : String, expirationDate : String?,
    vaccinationDate : String, validUntil : String, vet : Vet? = null) {
        medicalRecordRepository.addVaccination(medicalRecordId, batchNumber, manufacturer, name, expirationDate, vaccinationDate, validUntil, vet)
    }*/

    fun addVaccination (medicalRecordId : String?, batchNumber : String?, manufacturer : String, name : String, expirationDate : String?,
                         vaccinationDate : String, validUntil : String, vet : Vet? = null){
        authListener?.OnStarted()
        viewModelScope.launch{
            medicalRecordRepository.addVaccination(medicalRecordId, batchNumber, manufacturer, name, expirationDate, vaccinationDate, validUntil, vet, fun(){
                if(getStatusFromUpdateRecord().toString() == "fail"){
                    authListener?.OnFailure(null)
                }else{
                    authListener?.OnSuccess()
                }
            })
        }
    }

    fun addVermifugation (medicalRecordId : String?, manufacturer : String, name : String, expirationDate : String?,
                        vermifugationDate : String, validUntil : String, vet : Vet? = null) {
        authListener?.OnStarted()
        viewModelScope.launch{
            medicalRecordRepository.addVermifugation(medicalRecordId, manufacturer, name, expirationDate, vermifugationDate, validUntil, vet, fun(){
                if(getStatusFromUpdateRecord().toString() == "fail"){
                    authListener?.OnFailure(null)
                }else{
                    authListener?.OnSuccess()
                }
            })
        }
    }

    fun addTreatment(medicalRecordId : String?, medicine : String, disease : String, startOfTreatment : String,
                     endOfTreatment : String, frequency : String, vet : Vet? = null) {
        authListener?.OnStarted()
        viewModelScope.launch{
            medicalRecordRepository.addTreatment(medicalRecordId, medicine, disease, startOfTreatment, endOfTreatment, frequency, vet, fun(){
                if(getStatusFromUpdateRecord().toString() == "fail"){
                    authListener?.OnFailure(null)
                }else{
                    authListener?.OnSuccess()
                }
            })
        }
    }

    fun addSurgery(medicalRecordId : String?, name : String, date : String, vet : Vet? = null) {
        authListener?.OnStarted()
        viewModelScope.launch{
            medicalRecordRepository.addSurgery(medicalRecordId, name, date, vet, fun(){
                if(getStatusFromUpdateRecord().toString() == "fail"){
                    authListener?.OnFailure(null)
                }else{
                    authListener?.OnSuccess()
                }
            })
        }

    }

    fun addDiagnosticTest(medicalRecordId : String?, name : String, date : String, result: String, vet : Vet? = null) {
        authListener?.OnStarted()
        viewModelScope.launch{
            medicalRecordRepository.addDiagnosticTest(medicalRecordId, name, date, result, vet, fun(){
                if(getStatusFromUpdateRecord().toString() == "fail"){
                    authListener?.OnFailure(null)
                }else{
                    authListener?.OnSuccess()
                }
            })
        }
    }
}