package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class VacanteCreateResponse(
    val message: String,
    val vacante: VacanteResponse
)
