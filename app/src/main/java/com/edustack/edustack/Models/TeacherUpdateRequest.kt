package com.edustack.edustack.Models

data class TeacherUpdateRequest(
    val id: String,
    val address: String,
    val city: String,
    val contactNumber: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val speciality: String
)