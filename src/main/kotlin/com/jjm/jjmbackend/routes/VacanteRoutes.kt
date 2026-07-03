package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.VacanteCreateResponse
import com.jjm.jjmbackend.dto.VacanteRequest
import com.jjm.jjmbackend.middleware.requireRole
import com.jjm.jjmbackend.repositories.CompanyRepository
import com.jjm.jjmbackend.repositories.UserRepository
import com.jjm.jjmbackend.repositories.VacanteRepository
import com.jjm.jjmbackend.services.VacanteService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.vacanteRoutes() {

    val vacanteService = VacanteService(VacanteRepository(), UserRepository(), CompanyRepository())

    route("/vacantes") {

        get {
            val area = call.request.queryParameters["area"]
            val location = call.request.queryParameters["location"]
            val vacantes = vacanteService.getAll(area, location)
            call.respond(HttpStatusCode.OK, vacantes)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@get
            }
            val vacante = vacanteService.getById(id)
            if (vacante == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Vacante no encontrada"))
                return@get
            }
            call.respond(HttpStatusCode.OK, vacante)
        }

        post {
            val user = requireRole(call, "EMPRESA")
            val request = try {
                call.receive<VacanteRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }
            val vacante = vacanteService.create(user.userId, request)
            if (vacante == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al crear vacante"))
                return@post
            }
            val response = VacanteCreateResponse(
                message = "Vacante creada exitosamente",
                vacante = vacante
            )
            call.respond(HttpStatusCode.Created, response)
        }

        put("/{id}") {
            requireRole(call, "EMPRESA")
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@put
            }
            val request = try {
                call.receive<VacanteRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@put
            }
            val updated = vacanteService.update(id, request)
            if (!updated) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Vacante no encontrada"))
                return@put
            }
            call.respond(HttpStatusCode.OK, mapOf("message" to "Vacante actualizada"))
        }

        delete("/{id}") {
            requireRole(call, "EMPRESA")
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@delete
            }
            val deleted = vacanteService.delete(id)
            if (!deleted) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Vacante no encontrada"))
                return@delete
            }
            call.respond(HttpStatusCode.OK, mapOf("message" to "Vacante eliminada"))
        }

        get("/my") {
            val user = requireRole(call, "EMPRESA")
            val vacantes = vacanteService.getByCompany(user.userId)
            call.respond(HttpStatusCode.OK, vacantes)
        }
    }
}
