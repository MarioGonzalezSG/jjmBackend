package com.jjm.jjmbackend.middleware

import com.jjm.jjmbackend.services.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

data class AuthUser(val userId: Int, val email: String, val role: String)

suspend fun getAuthUser(call: ApplicationCall): AuthUser? {
    val authHeader = call.request.headers["Authorization"]
    if (authHeader == null || !authHeader.startsWith("Bearer ")) return null

    val token = authHeader.removePrefix("Bearer ")
    val decoded = JwtService.verifyToken(token) ?: return null

    return AuthUser(
        userId = decoded.getClaim("userId").asInt(),
        email = decoded.getClaim("email").asString(),
        role = decoded.getClaim("role").asString()
    )
}

suspend fun requireAuth(call: ApplicationCall): AuthUser {
    val user = getAuthUser(call)
    if (user == null) {
        call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token invalido o ausente"))
        throw Exception("Unauthorized")
    }
    return user
}

suspend fun requireRole(call: ApplicationCall, vararg roles: String): AuthUser {
    val user = requireAuth(call)
    if (user.role !in roles) {
        call.respond(HttpStatusCode.Forbidden, mapOf("error" to "No tienes permisos para esta accion"))
        throw Exception("Forbidden")
    }
    return user
}
