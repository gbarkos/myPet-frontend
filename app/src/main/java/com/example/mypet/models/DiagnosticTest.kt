package com.example.mypet.models

import java.util.*

data class DiagnosticTest(
    val name: String,
    val date: String,
    val result: String,
    val veterinarian: Vet,
    val _id: String
)

