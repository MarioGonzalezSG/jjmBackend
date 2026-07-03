package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.middleware.requireAuth
import com.jjm.jjmbackend.services.ExternalApiService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.externalRoutes() {
    val externalApiService = ExternalApiService()

    route("/external") {

        get("/job-market") {
            val data = externalApiService.fetchJobMarketData()
            call.respond(HttpStatusCode.OK, mapOf(
                "source" to "Proveedor de contenido externo",
                "data" to data
            ))
        }

        get("/content") {
            val content = externalApiService.fetchEducationalContent()
            call.respond(HttpStatusCode.OK, mapOf(
                "provider" to "Proveedor de contenido educativo",
                "content" to content
            ))
        }

        get("/vacancies") {
            val vacantes = externalApiService.fetchExternalVacancies()
            call.respond(HttpStatusCode.OK, mapOf(
                "source" to "Servicio web externo de vacantes",
                "vacancies" to vacantes
            ))
        }

        get("/geocode") {
            val address = call.request.queryParameters["address"] ?: "Lima, Peru"
            val result = externalApiService.geocodeAddress(address)
            if (result == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Direccion invalida"))
                return@get
            }
            call.respond(HttpStatusCode.OK, mapOf(
                "service" to "Acceso a servicio web de geocodificacion",
                "result" to result
            ))
        }

        get("/info") {
            call.respond(HttpStatusCode.OK, mapOf(
                "message" to "Servicios web externos y proveedores de contenido disponibles",
                "endpoints" to listOf(
                    mapOf("path" to "/external/job-market", "description" to "Datos de mercado laboral de proveedor externo"),
                    mapOf("path" to "/external/content", "description" to "Contenido educativo de proveedor externo"),
                    mapOf("path" to "/external/vacancies", "description" to "Vacantes de servicios web externos"),
                    mapOf("path" to "/external/geocode?address=...", "description" to "Geocodificacion mediante servicio web externo")
                )
            ))
        }
    }
}
