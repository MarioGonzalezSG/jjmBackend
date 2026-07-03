package com.jjm.jjmbackend.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val message: String,
    val timestamp: String,
    val vacanteId: Int? = null
)
