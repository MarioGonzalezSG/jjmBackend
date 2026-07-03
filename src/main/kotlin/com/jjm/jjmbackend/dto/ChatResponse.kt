package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: Int,
    val senderId: Int,
    val senderName: String,
    val receiverId: Int,
    val receiverName: String,
    val message: String,
    val timestamp: String,
    val vacanteId: Int? = null
)

@Serializable
data class ConversationResponse(
    val userId: Int,
    val userName: String,
    val lastMessage: String,
    val lastTimestamp: String
)
