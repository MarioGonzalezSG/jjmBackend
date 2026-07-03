package com.jjm.jjmbackend

import com.jjm.jjmbackend.config.configureSerialization
import com.jjm.jjmbackend.database.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*

fun Application.module() {

    DatabaseFactory.init()

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        anyHost()
    }

    configureSerialization()
    configureRouting()
}
