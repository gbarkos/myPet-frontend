package com.example.mypet.models

import java.util.*

data class Surgery(
    val name: String,
    val date: String,
    val veterinarian: Vet,
    val _id: String
)
