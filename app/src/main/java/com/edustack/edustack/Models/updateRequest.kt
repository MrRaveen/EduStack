package com.edustack.edustack.Models

import java.time.LocalDateTime

data class updateRequest (
    val id : String,
    val address: String,
    val city: String,
    val contactNumber: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val school: String,
    val userNameNew: String,
    val newPassword: String
)