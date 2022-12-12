package com.example.mypet.models

import java.util.*

data class Reminder(
        var user: String,
        var pet: Pet,
        var dateScheduled: String,
        var timeScheduled: String,
        var type: String,
        val _id: String,
)
