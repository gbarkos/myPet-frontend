package com.example.mypet.models

data class User (
    val username : String,
    val name : String,
    val surname : String,
    val email : String,
    val password: String,
    val phoneNumber : String,
    val address : String,
    val pets : List<Pet>,
    val receiveMissingMail : Boolean,
    val _id: String
)