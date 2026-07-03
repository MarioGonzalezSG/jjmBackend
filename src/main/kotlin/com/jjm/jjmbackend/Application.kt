package com.jjm.jjmbackend

import com.jjm.jjmbackend.config.configureSerialization
import com.jjm.jjmbackend.database.DatabaseFactory
import com.jjm.jjmbackend.repositories.NotificationRepository
import com.jjm.jjmbackend.repositories.UserRepository
import com.jjm.jjmbackend.repositories.VacanteRepository
import com.jjm.jjmbackend.services.BackgroundTaskService
import com.jjm.jjmbackend.services.NotificationService
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

    val notificationService = NotificationService(NotificationRepository(), UserRepository(), VacanteRepository())
    val backgroundTaskService = BackgroundTaskService(
        application = this,
        vacanteRepository = VacanteRepository(),
        notificationRepository = NotificationRepository(),
        notificationService = notificationService
    )
    backgroundTaskService.start()
}
