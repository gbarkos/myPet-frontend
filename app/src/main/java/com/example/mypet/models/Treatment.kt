package com.example.mypet.models

import java.util.*

data class Treatment(
    val medicine: String,
    val disease: String,
    val startOfTreatment: String,
    val endOfTreatment: String,
    val frequency: Int,
    val duration: Int,
    val veterinarian: Vet,
    val _id: String
)
