package com.example.mypet.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypet.models.*
import com.example.mypet.models.responses.MedicalRecordPatchResponse
import com.example.mypet.repositories.MedicalRecordRepository
import com.example.mypet.repositories.PetsRepository
import com.example.mypet.utils.Event
import com.example.mypet.utils.NetworkLoadingState

class MedicalRecordViewModel : ViewModel() {
    var _id : String? = null
    lateinit var vaccinations : List<Vaccination>
    lateinit var vermifugations : List<Vermifugation>
    lateinit var treatments : List<Treatment>
    lateinit var surgeries : List<Surgery>
    lateinit var diagnosticTests : List<DiagnosticTest>

    private val medicalRecordRepository : MedicalRecordRepository = MedicalRecordRepository

    fun getLoadStateFromRepo(): LiveData<Event<NetworkLoadingState>> {
        return medicalRecordRepository.loadState
    }

    fun getMedicalRecordFromRepo(): MutableLiveData<MedicalRecordPatchResponse> {
        return medicalRecordRepository.getMedicalRecordResponse()
    }

    fun addVaccination (medicalRecordId : String?, batchNumber : String?, manufacturer : String, name : String, expirationDate : String?,
    vaccinationDate : String, validUntil : String, vet : Vet? = null) {
        medicalRecordRepository.addVaccination(medicalRecordId, batchNumber, manufacturer, name, expirationDate, vaccinationDate, validUntil, vet)
    }

    fun addVermifugation (medicalRecordId : String?, manufacturer : String, name : String, expirationDate : String?,
                        vermifugationDate : String, validUntil : String, vet : Vet? = null) {
        medicalRecordRepository.addVermifugation(medicalRecordId, manufacturer, name, expirationDate, vermifugationDate, validUntil, vet)
    }

    fun addTreatment(medicalRecordId : String?, medicine : String, disease : String, startOfTreatment : String,
                     endOfTreatment : String, frequency : String, vet : Vet? = null) {
        medicalRecordRepository.addTreatment(medicalRecordId, medicine, disease, startOfTreatment, endOfTreatment, frequency, vet)
    }

    fun addSurgery(medicalRecordId : String?, name : String, date : String, vet : Vet? = null) {
        medicalRecordRepository.addSurgery(medicalRecordId, name, date, vet)
    }

    fun addDiagnosticTest(medicalRecordId : String?, name : String, date : String, result: String, vet : Vet? = null) {
        medicalRecordRepository.addDiagnosticTest(medicalRecordId, name, date, result, vet)
    }
}