package com.example.mypet.models

import java.util.*

data class Treatment(
    val medicine: String,
    val disease: String,
    val sartOfTreatment: Date,
    val endOfTreatment: Date,
    val frequency: Int,
    val veterinarian: Vet,
    val _id: String
)
