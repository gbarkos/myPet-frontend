package com.example.mypet.models

import java.util.*

data class Vermifugation(
    val manufacturer: String,
    val name: String,
    val expirationDate: String,
    val vermifugationDate: String,
    val validUntil: String,
    val veterinarian: Vet,
    val _id: String
)
