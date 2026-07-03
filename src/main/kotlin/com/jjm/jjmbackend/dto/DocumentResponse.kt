package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable
import com.jjm.jjmbackend.models.Document

@Serializable
data class DocumentResponse(
    val id: Int,
    val name: String,
    val type: String,
    val fileUrl: String,
    val uploadedBy: Int,
    val createdAt: String
)
