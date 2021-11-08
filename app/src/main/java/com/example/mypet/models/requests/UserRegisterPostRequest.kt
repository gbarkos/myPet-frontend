package com.example.mypet.models.requests

data class UserRegisterPostRequest(
    val username : String,
    val name : String,
    val surname : String,
    val email : String,
    val password: String,
    val confirmPassword: String,
    val phoneNumber : String,
    val address : String
)
