package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: Int,
    val email: String,
    val name: String,
    val role: String
)
