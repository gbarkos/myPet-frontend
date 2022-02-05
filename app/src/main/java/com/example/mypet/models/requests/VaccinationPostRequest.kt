package com.example.mypet.models.requests

import com.example.mypet.models.Vet
import java.util.*

data class VaccinationPostRequest(
    val batchNumber: String?,
    val manufacturer: String,
    val name: String,
    val expirationDate: String?,
    val vaccinationDate: String,
    val validUntil: String,
    val veterinarian: Vet?
)
