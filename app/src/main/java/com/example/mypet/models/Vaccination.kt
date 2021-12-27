package com.example.mypet.models

import java.util.*

data class Vaccination(
    val batchNumber: String,
    val manufacturer: String,
    val name: String,
    val expirationDate: String,
    val vaccinationDate: String,
    val validUntil: String,
    val veterinarian: Vet,
    val _id: String
)
