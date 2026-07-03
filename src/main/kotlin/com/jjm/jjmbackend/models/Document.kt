package com.jjm.jjmbackend.models

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val id: Int,
    val studentId: Int? = null,
    val vacanteId: Int? = null,
    val name: String,
    val type: String,
    val fileUrl: String,
    val uploadedBy: Int,
    val createdAt: String
)
