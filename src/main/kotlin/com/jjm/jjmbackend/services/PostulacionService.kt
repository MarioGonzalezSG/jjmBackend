package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.dto.PostulacionRequest
import com.jjm.jjmbackend.dto.PostulacionResponse
import com.jjm.jjmbackend.repositories.PostulacionRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostulacionService(
    private val postulacionRepository: PostulacionRepository
) {
    fun apply(studentId: Int, request: PostulacionRequest): PostulacionResponse? {
        return postulacionRepository.create(
            studentId = studentId,
            vacanteId = request.vacanteId,
            message = request.message,
            createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }

    fun getMyApplications(studentId: Int): List<PostulacionResponse> {
        return postulacionRepository.findByStudent(studentId)
    }

    fun getByVacante(vacanteId: Int): List<PostulacionResponse> {
        return postulacionRepository.findByVacante(vacanteId)
    }

    fun updateStatus(id: Int, status: String): Boolean {
        return postulacionRepository.updateStatus(id, status)
    }

    fun getAcceptedByCompany(companyId: Int): List<PostulacionResponse> {
        return postulacionRepository.findAcceptedByCompany(companyId)
    }
}
