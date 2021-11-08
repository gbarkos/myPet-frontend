package com.example.mypet.models

import java.util.*

data class DiagnosticTest(
    val name: String,
    val date: Date,
    val result: String,
    val veterinarian: Vet,
    val _id: String
)

