package com.edustack.edustack.Models

import com.google.firebase.Timestamp

data class CreateStudentAccRequest(
    val firstName: String,
    val lastName: String,
    val address: String,
    val city: String,
    val contactNumber: String,
    val email: String,
    val school: String,
    val dob: Timestamp,  // Changed to Timestamp type
    val gender: String,  // Added gender field
    val userName: String,
    val password: String
)