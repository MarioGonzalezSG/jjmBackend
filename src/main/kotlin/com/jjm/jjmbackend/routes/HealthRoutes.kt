package com.jjm.jjmbackend.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.healthRoutes() {

    get("/health") {
        call.respond(
            mapOf(
                "status" to "OK",
                "message" to "Backend running 🚀"
            )
        )
    }
}