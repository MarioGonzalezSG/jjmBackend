package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.ChatRequest
import com.jjm.jjmbackend.middleware.requireAuth
import com.jjm.jjmbackend.repositories.ChatRepository
import com.jjm.jjmbackend.services.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.chatRoutes() {

    val chatService = ChatService(ChatRepository())

    route("/chat") {

        post("/send") {
            val user = requireAuth(call)
            val request = try {
                call.receive<ChatRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }
            chatService.send(user.userId, request)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Mensaje enviado"))
        }

        get("/conversations") {
            val user = requireAuth(call)
            val conversations = chatService.getConversations(user.userId)
            call.respond(HttpStatusCode.OK, conversations)
        }

        get("/conversation/{userId}") {
            val user = requireAuth(call)
            val otherId = call.parameters["userId"]?.toIntOrNull()
            if (otherId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@get
            }
            val messages = chatService.getConversation(user.userId, otherId)
            call.respond(HttpStatusCode.OK, messages)
        }
    }
}
