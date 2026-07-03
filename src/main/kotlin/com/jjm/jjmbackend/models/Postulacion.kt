package com.jjm.jjmbackend.models

import kotlinx.serialization.Serializable

@Serializable
data class Postulacion(
    val id: Int,
    val studentId: Int,
    val vacanteId: Int,
    val status: String = "PENDIENTE",
    val message: String? = null,
    val createdAt: String
)
