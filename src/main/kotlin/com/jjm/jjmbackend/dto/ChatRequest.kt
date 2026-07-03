package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val receiverId: Int,
    val message: String,
    val vacanteId: Int? = null
)
