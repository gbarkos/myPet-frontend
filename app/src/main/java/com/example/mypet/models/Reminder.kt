package com.example.mypet.models

import java.util.*

data class Reminder(
        var user: User,
        var pet: Pet,
        var dateScheduled: Date,
        var timeScheduled: String,
        var type: String,
        val _id: String,
)
