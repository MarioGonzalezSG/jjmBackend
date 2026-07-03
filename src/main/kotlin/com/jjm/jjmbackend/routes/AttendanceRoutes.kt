package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.AttendanceRequest
import com.jjm.jjmbackend.middleware.requireRole
import com.jjm.jjmbackend.repositories.AttendanceRepository
import com.jjm.jjmbackend.services.AttendanceService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.attendanceRoutes() {

    val attendanceService = AttendanceService(AttendanceRepository())

    route("/attendance") {

        post {
            val user = requireRole(call, "EMPRESA")
            val request = try {
                call.receive<AttendanceRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }
            attendanceService.register(user.userId, request)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Asistencia registrada"))
        }

        get("/student/{studentId}") {
            val user = requireRole(call, "EMPRESA")
            val studentId = call.parameters["studentId"]?.toIntOrNull()
            if (studentId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@get
            }
            val records = attendanceService.getByStudent(studentId, user.userId)
            call.respond(HttpStatusCode.OK, records)
        }

        put("/{id}") {
            val user = requireRole(call, "EMPRESA")
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@put
            }
            val request = try {
                call.receive<AttendanceRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@put
            }
            val updated = attendanceService.update(id, request.status, request.notes)
            if (!updated) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Registro no encontrado"))
                return@put
            }
            call.respond(HttpStatusCode.OK, mapOf("message" to "Registro actualizado"))
        }
    }
}
