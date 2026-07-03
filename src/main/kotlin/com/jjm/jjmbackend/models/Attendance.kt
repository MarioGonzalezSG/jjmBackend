package com.jjm.jjmbackend.models

import kotlinx.serialization.Serializable

@Serializable
data class Attendance(
    val id: Int,
    val studentId: Int,
    val companyId: Int,
    val date: String,
    val status: String,
    val notes: String? = null
)
