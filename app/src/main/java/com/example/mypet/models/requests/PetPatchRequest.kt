package com.example.mypet.models.requests

data class PetPatchRequest(
    val id : String?,
    val distinguishingMarks : String?,
    val weight : String?,
    val height : String?,

)
