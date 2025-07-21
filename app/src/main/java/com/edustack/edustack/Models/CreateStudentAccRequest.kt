package com.edustack.edustack.Models

import java.time.LocalDateTime

data class CreateStudentAccRequest (
    val id : String,
    val address: String,
    val city: String,
    val contactNumber: String,
    val dob: LocalDateTime,
    val email: String,
    val firstName: String,
    val gender: String,
    val joinedDate: LocalDateTime,
    val lastName: String,
    val school: String,
    val UserName: String,
    val Password: String
)