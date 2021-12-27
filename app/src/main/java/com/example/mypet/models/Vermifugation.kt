package com.example.mypet.models

import java.util.*

data class Vermifugation(
    val manufaccturer: String,
    val name: String,
    val expirationDate: Date,
    val vermifugationDate: String,
    val validUntil: Date,
    val veterinarian: Vet,
    val _id: String
)
