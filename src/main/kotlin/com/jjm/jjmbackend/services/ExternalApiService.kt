package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.dto.ContentItem
import com.jjm.jjmbackend.dto.GeocodeResult
import com.jjm.jjmbackend.dto.JobMarketItem
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URI

class ExternalApiService {
    private val json = Json { ignoreUnknownKeys = true }

    fun fetchJobMarketData(): List<JobMarketItem> {
        return try {
            val url = URI.create("https://api.example.com/jobs/trending").toURL()
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            if (conn.responseCode == 200) {
                val body = conn.inputStream.bufferedReader().readText()
                json.decodeFromString<List<JobMarketItem>>(body)
            } else {
                getMockJobMarketData()
            }
        } catch (_: Exception) {
            getMockJobMarketData()
        }
    }

    fun fetchEducationalContent(): List<ContentItem> {
        return try {
            val url = URI.create("https://api.example.com/content/educational").toURL()
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            if (conn.responseCode == 200) {
                val body = conn.inputStream.bufferedReader().readText()
                json.decodeFromString<List<ContentItem>>(body)
            } else {
                getMockContent()
            }
        } catch (_: Exception) {
            getMockContent()
        }
    }

    fun geocodeAddress(address: String): GeocodeResult? {
        return try {
            val encoded = java.net.URLEncoder.encode(address, "UTF-8")
            val url = URI.create("https://api.example.com/geocode?q=$encoded").toURL()
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            if (conn.responseCode == 200) {
                val body = conn.inputStream.bufferedReader().readText()
                json.decodeFromString<GeocodeResult>(body)
            } else {
                mockGeocode(address)
            }
        } catch (_: Exception) {
            mockGeocode(address)
        }
    }

    fun fetchExternalVacancies(): List<Map<String, String>> {
        return try {
            val url = URI.create("https://api.example.com/vacancies/external").toURL()
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Accept", "application/json")
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            if (conn.responseCode == 200) {
                val body = conn.inputStream.bufferedReader().readText()
                json.decodeFromString<List<Map<String, String>>>(body)
            } else {
                getMockExternalVacancies()
            }
        } catch (_: Exception) {
            getMockExternalVacancies()
        }
    }

    private fun getMockJobMarketData(): List<JobMarketItem> = listOf(
        JobMarketItem("Desarrollador Kotlin", "Alta demanda", "Sector TI"),
        JobMarketItem("Analista de Datos", "Demanda media", "Sector TI"),
        JobMarketItem("Ingeniero de Software", "Alta demanda", "Sector TI"),
        JobMarketItem("Especialista en Ciberseguridad", "Demanda creciente", "Sector TI"),
        JobMarketItem("Administrador de Base de Datos", "Demanda estable", "Sector TI")
    )

    private fun getMockContent(): List<ContentItem> = listOf(
        ContentItem("Guia de Kotlin", "Aprende los fundamentos de Kotlin para desarrollo backend", "Tutorial"),
        ContentItem("Introduccion a Ktor", "Crea APIs web con el framework Ktor", "Documentacion"),
        ContentItem("Base de Datos con Exposed", "Guia de uso de Exposed ORM en Kotlin", "Tutorial"),
        ContentItem("Arquitectura REST", "Principios de diseno de APIs RESTful", "Articulo"),
        ContentItem("Seguridad en APIs", "Implementacion de autenticacion JWT", "Guia")
    )

    private fun getMockExternalVacancies(): List<Map<String, String>> = listOf(
        mapOf("title" to "Programador Junior", "company" to "TechCorp", "location" to "Remoto", "source" to "ProveedorExterno"),
        mapOf("title" to "Desarrollador Web", "company" to "WebSolutions", "location" to "Presencial", "source" to "ProveedorExterno"),
        mapOf("title" to "Practicante TI", "company" to "Innovatech", "location" to "Mixto", "source" to "ProveedorExterno")
    )

    private fun mockGeocode(address: String): GeocodeResult? {
        if (address.isBlank()) return null
        return GeocodeResult(
            latitude = -12.0464,
            longitude = -77.0428,
            address = address
        )
    }
}
