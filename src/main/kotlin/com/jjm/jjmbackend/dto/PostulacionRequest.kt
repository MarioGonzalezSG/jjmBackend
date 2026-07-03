package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostulacionRequest(
    val vacanteId: Int,
    val message: String? = null
)
