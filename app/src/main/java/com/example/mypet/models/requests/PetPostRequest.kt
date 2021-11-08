package com.example.mypet.models.requests

import java.util.*

data class PetPostRequest (
    val id : String,
    val name : String,
    val birthdate : Date,
    val colour : String,
    val distinguishingMarks : String,
    val breed : String,
    val sex : String,
    val photo : String,
    val weight : Double,
    val height : Double
)
