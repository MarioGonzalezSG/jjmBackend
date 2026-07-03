package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceResponse(
    val id: Int,
    val studentId: Int,
    val studentName: String,
    val date: String,
    val status: String,
    val notes: String? = null
)
