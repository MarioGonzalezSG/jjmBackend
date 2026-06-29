package com.jjm.jjmbackend

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.jjm.jjmbackend.routes.authRoutes
import com.jjm.jjmbackend.routes.healthRoutes

fun Application.configureRouting() {

    routing {
        healthRoutes()

        authRoutes()
        
    }
}