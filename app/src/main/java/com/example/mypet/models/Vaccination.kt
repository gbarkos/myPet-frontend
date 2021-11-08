package com.example.mypet.models

import java.util.*

data class Vaccination(
    val batchNumber: String,
    val manufaccturer: String,
    val name: String,
    val expirationDate: Date,
    val vaccinationDate: Date,
    val validUntil: Date,
    val veterinarian: Vet,
    val _id: String
)
