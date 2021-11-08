package com.example.mypet.models.requests

data class VetRegisterPostRequest(
    val username : String,
    val name : String,
    val surname : String,
    val email : String,
    val password: String,
    val phoneNumber : String,
    val clinicAddress : String
)
