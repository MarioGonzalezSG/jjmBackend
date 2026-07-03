package com.jjm.jjmbackend

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.jjm.jjmbackend.routes.*

fun Application.configureRouting() {

    routing {
        healthRoutes()
        authRoutes()
        companyRoutes()
        vacanteRoutes()
        postulacionRoutes()
        documentRoutes()
        attendanceRoutes()
        chatRoutes()
    }
}
