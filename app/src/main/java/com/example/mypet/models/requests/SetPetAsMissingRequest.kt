package com.example.mypet.models.requests

data class SetPetAsMissingRequest(
    val lat: String,
    val lng: String,
    val contactInfo: List<String>
)
