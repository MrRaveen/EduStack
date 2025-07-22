package com.edustack.edustack.Models

import java.util.Date

data class CalendarEvent(
    val id: String = "",
    val courseId: String = "",
    val hallId: String = "",
    val date: Date = Date(),
    val startTime: Date = Date(),
    val endTime: Date = Date(),
    val status: Boolean = true,
    val courseName: String = "",
    val description: String = "",
    val teacherName: String = ""
)