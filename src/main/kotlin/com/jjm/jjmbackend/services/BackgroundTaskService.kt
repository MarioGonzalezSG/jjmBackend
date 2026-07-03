package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.repositories.NotificationRepository
import com.jjm.jjmbackend.repositories.VacanteRepository
import io.ktor.server.application.*
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class BackgroundTaskService(
    private val application: Application,
    private val vacanteRepository: VacanteRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationService: NotificationService
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun start() {
        application.log.info("Iniciando BackgroundTaskService...")

        scope.launch {
            while (isActive) {
                delay(60_000L)
                closeExpiredVacancies()
            }
        }

        scope.launch {
            while (isActive) {
                delay(300_000L)
                sendDailySummary()
            }
        }

        application.log.info("BackgroundTaskService iniciado correctamente")
    }

    fun stop() {
        job.cancel()
        application.log.info("BackgroundTaskService detenido")
    }

    private fun closeExpiredVacancies() {
        try {
            val activeVacantes = vacanteRepository.findAllActive()
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            var closedCount = 0

            for (vacante in activeVacantes) {
                try {
                    val createdAt = LocalDateTime.parse(vacante.createdAt, formatter)
                    val daysActive = ChronoUnit.DAYS.between(createdAt, now)
                    if (daysActive >= 30) {
                        vacanteRepository.updateStatus(vacante.id, "CERRADA")
                        notificationService.notifyVacancyClosed(
                            companyId = vacante.companyId,
                            vacanteTitle = vacante.title,
                            reason = "ha pasado mas de 30 dias desde su creacion"
                        )
                        closedCount++
                    }
                } catch (_: Exception) { }
            }

            if (closedCount > 0) {
                application.log.info("Tarea fondo: $closedCount vacantes cerradas por expiracion")
            }
        } catch (e: Exception) {
            application.log.error("Error en tarea de cierre de vacantes: ${e.message}")
        }
    }

    private fun sendDailySummary() {
        try {
            val activeCount = vacanteRepository.countActive()
            application.log.info("Tarea fondo: Resumen diario - $activeCount vacantes activas")
        } catch (e: Exception) {
            application.log.error("Error en tarea de resumen diario: ${e.message}")
        }
    }
}
