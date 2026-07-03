package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostulacionResponse(
    val id: Int,
    val studentId: Int,
    val studentName: String,
    val studentEmail: String,
    val vacanteId: Int,
    val vacanteTitle: String,
    val status: String,
    val message: String? = null,
    val createdAt: String
)

@Serializable
data class PostulacionStatusRequest(
    val status: String
)
