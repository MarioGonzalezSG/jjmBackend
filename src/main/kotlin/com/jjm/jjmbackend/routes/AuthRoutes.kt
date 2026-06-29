package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.AuthResponse
import com.jjm.jjmbackend.dto.LoginRequest
import com.jjm.jjmbackend.dto.RegisterRequest
import com.jjm.jjmbackend.repositories.UserRepository
import com.jjm.jjmbackend.services.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {

    val authService = AuthService(UserRepository())

    route("/auth") {

        // 🔹 REGISTER
        post("/register") {

            val request = try {
                call.receive<RegisterRequest>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid request body")
                )
                return@post
            }

            // Validación básica de entrada
            if (request.email.isBlank() || !request.email.contains("@")) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid email")
                )
                return@post
            }

            if (request.password.length < 6) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Password must be at least 6 characters")
                )
                return@post
            }

            if (request.name.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Name is required")
                )
                return@post
            }

            val user = authService.register(request)

            if (user == null) {
                call.respond(
                    HttpStatusCode.Conflict,
                    mapOf("error" to "Email already exists")
                )
                return@post
            }

            call.respond(
                HttpStatusCode.Created,
                AuthResponse(token = null, user = user)
            )
        }

        // 🔹 LOGIN
        post("/login") {

            val request = try {
                call.receive<LoginRequest>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid request body")
                )
                return@post
            }

            if (request.email.isBlank() || request.password.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Email and password are required")
                )
                return@post
            }

            val user = authService.login(request)

            if (user == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Invalid credentials")
                )
                return@post
            }

            call.respond(
                HttpStatusCode.OK,
                AuthResponse(token = null, user = user)
            )
        }
    }
}