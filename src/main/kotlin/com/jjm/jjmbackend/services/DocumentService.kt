package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.dto.DocumentRequest
import com.jjm.jjmbackend.models.Document
import com.jjm.jjmbackend.repositories.DocumentRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DocumentService(
    private val documentRepository: DocumentRepository
) {
    fun upload(request: DocumentRequest, uploadedBy: Int): Document? {
        return documentRepository.create(
            name = request.name,
            type = request.type,
            fileUrl = request.fileUrl,
            uploadedBy = uploadedBy,
            studentId = request.studentId,
            vacanteId = request.vacanteId,
            createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }

    fun getMyDocuments(studentId: Int): List<Document> {
        return documentRepository.findByStudent(studentId)
    }

    fun getByVacante(vacanteId: Int): List<Document> {
        return documentRepository.findByVacante(vacanteId)
    }

    fun getOfficial(): List<Document> {
        return documentRepository.findByType("OFICIAL")
    }
}
