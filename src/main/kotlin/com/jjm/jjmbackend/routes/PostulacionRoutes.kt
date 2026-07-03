package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.PostulacionRequest
import com.jjm.jjmbackend.dto.PostulacionStatusRequest
import com.jjm.jjmbackend.middleware.requireRole
import com.jjm.jjmbackend.repositories.NotificationRepository
import com.jjm.jjmbackend.repositories.PostulacionRepository
import com.jjm.jjmbackend.repositories.UserRepository
import com.jjm.jjmbackend.repositories.VacanteRepository
import com.jjm.jjmbackend.services.NotificationService
import com.jjm.jjmbackend.services.PostulacionService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.postulacionRoutes() {

    val notificationService = NotificationService(NotificationRepository(), UserRepository(), VacanteRepository())
    val postulacionService = PostulacionService(PostulacionRepository(), VacanteRepository(), notificationService)

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
                val yaPostulado = PostulacionRepository().findByStudentAndVacante(user.userId, request.vacanteId)
                if (yaPostulado != null) {
                    call.respond(HttpStatusCode.Conflict, mapOf("error" to "Ya te has postulado a esta vacante anteriormente"))
                } else {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al postular"))
                }
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
            val user = requireRole(call, "EMPRESA")
            val vacanteId = call.parameters["vacanteId"]?.toIntOrNull()
            if (vacanteId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@get
            }
            val vacante = VacanteRepository().findById(vacanteId)
            if (vacante == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Vacante no encontrada"))
                return@get
            }
            if (vacante.companyId != user.userId) {
                call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Esta vacante no te pertenece"))
                return@get
            }
            val postulaciones = postulacionService.getByVacante(vacanteId)
            call.respond(HttpStatusCode.OK, postulaciones)
        }

        put("/{id}/status") {
            val user = requireRole(call, "EMPRESA")
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
            if (request.status !in listOf("ACEPTADA", "RECHAZADA", "PENDIENTE")) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Estado invalido. Use: ACEPTADA, RECHAZADA, PENDIENTE"))
                return@put
            }
            val postulacion = PostulacionRepository().findById(id)
            if (postulacion == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Postulacion no encontrada"))
                return@put
            }
            val vacante = VacanteRepository().findById(postulacion.vacanteId)
            if (vacante == null || vacante.companyId != user.userId) {
                call.respond(HttpStatusCode.Forbidden, mapOf("error" to "No tienes permiso para modificar esta postulacion"))
                return@put
            }
            val updated = postulacionService.updateStatus(id, request.status)
            if (updated == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al actualizar estado"))
                return@put
            }
            call.respond(HttpStatusCode.OK, updated)
        }

        get("/empresa") {
            val user = requireRole(call, "EMPRESA")
            val postulaciones = postulacionService.getAllByCompany(user.userId)
            call.respond(HttpStatusCode.OK, postulaciones)
        }

        get("/mis-estudiantes") {
            val user = requireRole(call, "EMPRESA")
            val estudiantes = postulacionService.getAcceptedByCompany(user.userId)
            call.respond(HttpStatusCode.OK, estudiantes)
        }
    }
}
