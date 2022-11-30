package com.example.mypet.models.responses

import com.example.mypet.models.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserLoginRegisterPostResponse (
    @SerializedName("status")
    @Expose()
    val status : String,
    @SerializedName("token")
    @Expose()
    val token : String,
    @SerializedName("user")
    @Expose()
    val user : User,
)

