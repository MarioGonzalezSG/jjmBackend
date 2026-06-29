package com.jjm.jjmbackend

import com.jjm.jjmbackend.config.configureSerialization
import com.jjm.jjmbackend.database.DatabaseFactory
import io.ktor.server.application.*

fun Application.module() {

    DatabaseFactory.init()

    configureSerialization()
    configureRouting()
}