package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateNotificationRequest(
    val userId: Int,
    val title: String,
    val message: String,
    val type: String = "SYSTEM",
    val relatedId: Int? = null
)
