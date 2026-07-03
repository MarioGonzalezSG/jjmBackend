package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequest(
    val studentId: Int,
    val date: String,
    val status: String,
    val notes: String? = null
)
