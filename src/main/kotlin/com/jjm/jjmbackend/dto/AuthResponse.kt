package com.jjm.jjmbackend.dto

import com.jjm.jjmbackend.models.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String? = null,
    val user: User,
)