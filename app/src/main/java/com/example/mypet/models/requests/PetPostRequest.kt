package com.example.mypet.models.requests

import java.util.*

data class PetPostRequest (
    val id : String?,
    val name : String,
    val birthdate : String,
    val colour : String,
    val distinguishingMarks : String?,
    val breed : String,
    val sex : String,
    val weight : String?,
    val height : String?,
    var species : String,
    val isMissing : Boolean = false,
)
