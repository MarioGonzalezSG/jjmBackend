package com.jjm.jjmbackend.models

import kotlinx.serialization.Serializable

@Serializable
data class Vacante(
    val id: Int,
    val companyId: Int,
    val title: String,
    val description: String,
    val requirements: String? = null,
    val slots: Int,
    val area: String? = null,
    val duration: String? = null,
    val schedule: String? = null,
    val location: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: String = "ACTIVA",
    val createdAt: String
)
