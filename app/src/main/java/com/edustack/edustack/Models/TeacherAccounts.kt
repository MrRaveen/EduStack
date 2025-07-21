package com.edustack.edustack.Models

import java.time.LocalDateTime

data class TeacherAccounts(
    val id: String = "",
    val Address: String = "",
    val City: String = "",
    val ContactNumber: String = "",
    val DOB: LocalDateTime,
    val Email: String = "",
    val FirstName: String = "",
    val Gender: String = "",
    val JoinedDate: LocalDateTime,
    val LastName: String = "",
    val Speciality: String = ""
)