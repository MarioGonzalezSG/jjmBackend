package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.CompanyRequest
import com.jjm.jjmbackend.middleware.requireAuth
import com.jjm.jjmbackend.middleware.requireRole
import com.jjm.jjmbackend.repositories.CompanyRepository
import com.jjm.jjmbackend.repositories.UserRepository
import com.jjm.jjmbackend.services.CompanyService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.companyRoutes() {

    val companyService = CompanyService(CompanyRepository(), UserRepository())

    route("/companies") {

        get("/map") {
            val companies = companyService.getAllWithLocation()
            call.respond(HttpStatusCode.OK, companies)
        }

        get("/profile") {
            val user = requireAuth(call)
            val profile = companyService.getProfile(user.userId)
            if (profile == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Perfil no encontrado"))
                return@get
            }
            call.respond(HttpStatusCode.OK, profile)
        }

        put("/profile") {
            val user = requireRole(call, "EMPRESA")
            val request = try {
                call.receive<CompanyRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@put
            }
            val profile = companyService.updateProfile(user.userId, request)
            call.respond(HttpStatusCode.OK, profile!!)
        }
    }
}
