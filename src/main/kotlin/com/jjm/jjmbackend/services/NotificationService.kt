package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.repositories.NotificationRepository
import com.jjm.jjmbackend.repositories.UserRepository
import com.jjm.jjmbackend.repositories.VacanteRepository

class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val vacanteRepository: VacanteRepository
) {
    fun notifyCompanyOnApplication(studentId: Int, vacanteId: Int, companyId: Int) {
        val student = userRepository.findById(studentId) ?: return
        val vacante = vacanteRepository.findById(vacanteId) ?: return
        notificationRepository.create(
            userId = companyId,
            title = "Nueva postulacion",
            message = "El estudiante ${student.name} se ha postulado a tu vacante '${vacante.title}'",
            type = "POSTULACION",
            relatedId = vacanteId
        )
    }

    fun notifyStudentOnStatusChange(studentId: Int, vacanteTitle: String, status: String) {
        val statusMsg = when (status) {
            "ACEPTADA" -> "aceptada"
            "RECHAZADA" -> "rechazada"
            "PENDIENTE" -> "puesta en espera"
            else -> status
        }
        notificationRepository.create(
            userId = studentId,
            title = "Estado de postulacion actualizado",
            message = "Tu postulacion para '${vacanteTitle}' ha sido ${statusMsg}",
            type = "POSTULACION",
            relatedId = null
        )
    }

    fun notifyVacancyClosed(companyId: Int, vacanteTitle: String, reason: String) {
        notificationRepository.create(
            userId = companyId,
            title = "Vacante cerrada",
            message = "Tu vacante '${vacanteTitle}' ha sido cerrada automaticamente: ${reason}",
            type = "SYSTEM",
            relatedId = null
        )
    }
}
