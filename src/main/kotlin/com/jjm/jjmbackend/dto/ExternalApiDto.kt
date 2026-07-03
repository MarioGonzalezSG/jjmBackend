package com.jjm.jjmbackend.dto

import kotlinx.serialization.Serializable

@Serializable
data class JobMarketItem(
    val title: String,
    val demand: String,
    val sector: String
)

@Serializable
data class ContentItem(
    val title: String,
    val description: String,
    val type: String
)

@Serializable
data class GeocodeResult(
    val latitude: Double,
    val longitude: Double,
    val address: String
)
