package com.example.mypet.models

import com.example.mypet.models.enums.ReminderTypes
import java.util.*

data class Reminder(
        var user: User,
        var pet: Pet,
        var dateScheduled: Date,
        var type: ReminderTypes,
        val _id: String,
)
