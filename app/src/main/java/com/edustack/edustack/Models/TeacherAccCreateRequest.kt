package com.edustack.edustack.Models

import java.time.LocalDateTime

data class TeacherAccCreateRequest (
    val address: String,
    val city: String,
    val contactNumber: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val speciality: String,
    val UserName: String,
    val Password: String,
    val DOB: LocalDateTime,
)