package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable
import com.jjm.jjmbackend.models.Vacante

@Serializable
data class VacanteResponse(
    val id: Int,
    val companyId: Int,
    val companyName: String,
    val title: String,
    val description: String,
    val requirements: String? = null,
    val slots: Int,
    val area: String? = null,
    val duration: String? = null,
    val schedule: String? = null,
    val location: String? = null,
    val status: String = "ACTIVA",
    val createdAt: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)
