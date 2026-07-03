package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.dto.PostulacionRequest
import com.jjm.jjmbackend.dto.PostulacionResponse
import com.jjm.jjmbackend.repositories.PostulacionRepository
import com.jjm.jjmbackend.repositories.VacanteRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostulacionService(
    private val postulacionRepository: PostulacionRepository,
    private val vacanteRepository: VacanteRepository,
    private val notificationService: NotificationService
) {
    fun apply(studentId: Int, request: PostulacionRequest): PostulacionResponse? {
        val existing = postulacionRepository.findByStudentAndVacante(studentId, request.vacanteId)
        if (existing != null) return null

        val postulacion = postulacionRepository.create(
            studentId = studentId,
            vacanteId = request.vacanteId,
            message = request.message,
            createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        ) ?: return null

        val vacante = vacanteRepository.findById(request.vacanteId)
        if (vacante != null) {
            notificationService.notifyCompanyOnApplication(studentId, request.vacanteId, vacante.companyId)
        }

        return postulacion
    }

    fun getMyApplications(studentId: Int): List<PostulacionResponse> {
        return postulacionRepository.findByStudent(studentId)
    }

    fun getByVacante(vacanteId: Int): List<PostulacionResponse> {
        return postulacionRepository.findByVacante(vacanteId)
    }

    fun updateStatus(id: Int, status: String): PostulacionResponse? {
        val postulacion = postulacionRepository.findById(id)
        if (postulacion == null) return null

        val updated = postulacionRepository.updateStatus(id, status) ?: return null
        val vacante = vacanteRepository.findById(postulacion.vacanteId)
        val vacanteTitle = vacante?.title ?: "Vacante"
        notificationService.notifyStudentOnStatusChange(postulacion.studentId, vacanteTitle, status)
        return updated
    }

    fun getAllByCompany(companyId: Int): List<PostulacionResponse> {
        return postulacionRepository.findAllByCompany(companyId)
    }

    fun getAcceptedByCompany(companyId: Int): List<PostulacionResponse> {
        return postulacionRepository.findAcceptedByCompany(companyId)
    }
}
