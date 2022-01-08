package com.example.mypet.models

import java.util.*

data class Pet(
    val id : String,
    val name : String,
    val birthdate : String,
    val colour : String,
    val distinguishingMarks : String,
    val breed : String,
    val sex : String,
    val photo : String,
    val weight : Double,
    val height : Double,
    val medicalRecord : MedicalRecord,
    val owner: String,
    val _id: String
)
