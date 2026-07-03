package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.PostulacionRequest
import com.jjm.jjmbackend.dto.PostulacionStatusRequest
import com.jjm.jjmbackend.middleware.requireRole
import com.jjm.jjmbackend.repositories.PostulacionRepository
import com.jjm.jjmbackend.services.PostulacionService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.postulacionRoutes() {

    val postulacionService = PostulacionService(PostulacionRepository())

    route("/postulaciones") {

        post {
            val user = requireRole(call, "ESTUDIANTE")
            val request = try {
                call.receive<PostulacionRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }
            val postulacion = postulacionService.apply(user.userId, request)
            if (postulacion == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al postular"))
                return@post
            }
            call.respond(HttpStatusCode.Created, postulacion)
        }

        get("/mis-postulaciones") {
            val user = requireRole(call, "ESTUDIANTE")
            val postulaciones = postulacionService.getMyApplications(user.userId)
            call.respond(HttpStatusCode.OK, postulaciones)
        }

        get("/vacante/{vacanteId}") {
            requireRole(call, "EMPRESA")
            val vacanteId = call.parameters["vacanteId"]?.toIntOrNull()
            if (vacanteId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@get
            }
            val postulaciones = postulacionService.getByVacante(vacanteId)
            call.respond(HttpStatusCode.OK, postulaciones)
        }

        put("/{id}/status") {
            requireRole(call, "EMPRESA")
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@put
            }
            val request = try {
                call.receive<PostulacionStatusRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@put
            }
            if (request.status !in listOf("ACEPTADA", "RECHAZADA")) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Estado invalido"))
                return@put
            }
            val updated = postulacionService.updateStatus(id, request.status)
            if (!updated) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Postulacion no encontrada"))
                return@put
            }
            call.respond(HttpStatusCode.OK, mapOf("message" to "Estado actualizado"))
        }

        get("/mis-estudiantes") {
            val user = requireRole(call, "EMPRESA")
            val estudiantes = postulacionService.getAcceptedByCompany(user.userId)
            call.respond(HttpStatusCode.OK, estudiantes)
        }
    }
}
