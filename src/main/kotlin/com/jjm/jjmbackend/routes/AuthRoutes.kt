package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.LoginRequest
import com.jjm.jjmbackend.dto.RegisterRequest
import com.jjm.jjmbackend.middleware.getAuthUser
import com.jjm.jjmbackend.repositories.CompanyRepository
import com.jjm.jjmbackend.repositories.UserRepository
import com.jjm.jjmbackend.services.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {

    val authService = AuthService(UserRepository(), CompanyRepository())

    route("/auth") {

        post("/register") {
            val request = try {
                call.receive<RegisterRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }

            if (request.email.isBlank() || !request.email.contains("@")) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Correo invalido"))
                return@post
            }
            if (request.password.length < 6) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "La contrasena debe tener al menos 6 caracteres"))
                return@post
            }
            if (request.name.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "El nombre es requerido"))
                return@post
            }
            if (request.role !in listOf("ESTUDIANTE", "EMPRESA", "ADMINISTRADOR")) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Rol invalido"))
                return@post
            }

            val result = authService.register(request)
            if (result == null) {
                call.respond(HttpStatusCode.Conflict, mapOf("error" to "El correo ya esta registrado"))
                return@post
            }

            call.respond(HttpStatusCode.Created, result)
        }

        post("/login") {
            val request = try {
                call.receive<LoginRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }

            if (request.email.isBlank() || request.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Correo y contrasena requeridos"))
                return@post
            }

            val result = authService.login(request)
            if (result == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Credenciales invalidas"))
                return@post
            }

            call.respond(HttpStatusCode.OK, result)
        }

        get("/me") {
            val user = getAuthUser(call)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No autenticado"))
                return@get
            }
            val fullUser = authService.getUserFromToken(user.userId)
            if (fullUser == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Usuario no encontrado"))
                return@get
            }
            call.respond(HttpStatusCode.OK, fullUser)
        }
    }
}
