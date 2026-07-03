package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val role: String = "ESTUDIANTE"
)
