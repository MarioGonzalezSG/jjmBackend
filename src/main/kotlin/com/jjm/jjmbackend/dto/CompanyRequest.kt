package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompanyRequest(
    val description: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
