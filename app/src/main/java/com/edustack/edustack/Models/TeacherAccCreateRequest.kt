package com.edustack.edustack.Models

import com.google.firebase.Timestamp

data class CreateTeacherAccRequest(
    val firstName: String,
    val lastName: String,
    val address: String,
    val city: String,
    val contactNumber: String,
    val email: String,
    val speciality: String,  // Changed from school to speciality
    val dob: Timestamp,
    val gender: String,
    val userName: String,
    val password: String
)