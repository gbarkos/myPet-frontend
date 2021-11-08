package com.example.mypet.models

data class Vet(
    val username : String,
    val name : String,
    val surname : String,
    val email : String,
    val password: String,
    val phoneNumber : String,
    val clinicAddress : String,
    val _id: String = ""
)
