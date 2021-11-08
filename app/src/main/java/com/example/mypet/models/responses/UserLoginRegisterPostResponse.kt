package com.example.mypet.models.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserLoginRegisterPostResponse (
    @SerializedName("status")
    @Expose()
    val status : String,
    @SerializedName("token")
    @Expose()
    val token : String
)

