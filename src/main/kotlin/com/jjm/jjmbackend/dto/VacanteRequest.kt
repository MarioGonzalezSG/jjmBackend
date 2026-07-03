package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class VacanteRequest(
    val title: String,
    val description: String,
    val requirements: String? = null,
    val slots: Int,
    val area: String? = null,
    val duration: String? = null,
    val schedule: String? = null,
    val location: String? = null
)
