package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class DocumentRequest(
    val name: String,
    val type: String,
    val fileUrl: String,
    val studentId: Int? = null,
    val vacanteId: Int? = null
)
