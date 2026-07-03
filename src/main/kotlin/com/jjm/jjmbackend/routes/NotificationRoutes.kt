package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.CreateNotificationRequest
import com.jjm.jjmbackend.middleware.getAuthUser
import com.jjm.jjmbackend.repositories.NotificationRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.notificationRoutes() {

    val notificationRepository = NotificationRepository()

    route("/notifications") {

        get {
            val user = getAuthUser(call)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No autenticado"))
                return@get
            }
            val notifications = notificationRepository.findByUserId(user.userId)
            call.respond(HttpStatusCode.OK, notifications)
        }

        get("/unread-count") {
            val user = getAuthUser(call)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No autenticado"))
                return@get
            }
            val count = notificationRepository.countUnread(user.userId)
            call.respond(HttpStatusCode.OK, mapOf("count" to count))
        }

        post {
            val user = getAuthUser(call)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No autenticado"))
                return@post
            }
            val request = try {
                call.receive<CreateNotificationRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }
            if (request.userId != user.userId) {
                call.respond(HttpStatusCode.Forbidden, mapOf("error" to "No puedes crear notificaciones para otro usuario"))
                return@post
            }
            val notification = notificationRepository.create(
                userId = request.userId,
                title = request.title,
                message = request.message,
                type = request.type,
                relatedId = request.relatedId
            )
            if (notification == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al crear notificacion"))
                return@post
            }
            call.respond(HttpStatusCode.Created, notification)
        }

        put("/{id}/read") {
            val user = getAuthUser(call)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No autenticado"))
                return@put
            }
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@put
            }
            val updated = notificationRepository.markAsRead(id)
            if (!updated) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Notificacion no encontrada"))
                return@put
            }
            call.respond(HttpStatusCode.OK, mapOf("message" to "Marcada como leida"))
        }

        put("/read-all") {
            val user = getAuthUser(call)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No autenticado"))
                return@put
            }
            notificationRepository.markAllAsRead(user.userId)
            call.respond(HttpStatusCode.OK, mapOf("message" to "Todas marcadas como leidas"))
        }
    }
}
