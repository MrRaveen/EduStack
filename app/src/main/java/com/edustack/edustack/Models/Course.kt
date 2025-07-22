package com.edustack.edustack.Models

import com.google.firebase.firestore.FieldValue

data class Course(
    val id: String = "",
    val DefaultHall: String = "",
    val Description: String = "",
    val EndTime: String = "",
    val Name: String = "",
    val StartTime: String = "",
    val StartedDate: Any = FieldValue.serverTimestamp(),
    val Status: Boolean = true,
    val TeacherID: String = "",
    val WeekDay: String = "",
    val isCanceled: Boolean = false
)
