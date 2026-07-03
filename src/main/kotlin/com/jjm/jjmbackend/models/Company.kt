package com.jjm.jjmbackend.models

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val id: Int,
    val userId: Int,
    val description: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
