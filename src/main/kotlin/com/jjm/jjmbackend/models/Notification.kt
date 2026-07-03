package com.jjm.jjmbackend.models

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: Int,
    val userId: Int,
    val title: String,
    val message: String,
    val type: String = "SYSTEM",
    val isRead: Boolean = false,
    val relatedId: Int? = null,
    val createdAt: String
)
