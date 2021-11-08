package com.example.mypet.models.requests

import com.example.mypet.models.Vet
import java.util.*

data class VermifugationPatchRequest (
    val batchNumber: String,
    val manufaccturer: String,
    val name: String,
    val expirationDate: Date,
    val vaccinationDate: Date,
    val validUntil: Date,
    val veterinarian: Vet
)