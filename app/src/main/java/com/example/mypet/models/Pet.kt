package com.example.mypet.models

data class Pet(
    val id : String?,
    val name : String,
    val birthdate : String,
    val colour : String,
    val distinguishingMarks : String?,
    val breed : String,
    val sex : String,
    val photo : String?,
    val weight : String?,
    val height : String?,
    val medicalRecord : MedicalRecord,
    val owner: String,
    val _id: String,
    var species : String,
    val isMissing : Boolean
)
