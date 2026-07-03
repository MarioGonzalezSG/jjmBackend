package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.DocumentsTable
import com.jjm.jjmbackend.models.Document
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DocumentRepository {

    fun create(
        name: String, type: String, fileUrl: String,
        uploadedBy: Int, studentId: Int?, vacanteId: Int?, createdAt: String
    ): Document? {
        return transaction {
            val insert = DocumentsTable.insert {
                it[DocumentsTable.name] = name
                it[DocumentsTable.type] = type
                it[DocumentsTable.fileUrl] = fileUrl
                it[DocumentsTable.uploadedBy] = uploadedBy
                it[DocumentsTable.studentId] = studentId
                it[DocumentsTable.vacanteId] = vacanteId
                it[DocumentsTable.createdAt] = createdAt
            }
            insert.resultedValues?.firstOrNull()?.let { mapRow(it) }
        }
    }

    fun findByStudent(studentId: Int): List<Document> {
        return transaction {
            DocumentsTable.select { DocumentsTable.studentId eq studentId }
                .mapNotNull { mapRow(it) }
        }
    }

    fun findByVacante(vacanteId: Int): List<Document> {
        return transaction {
            DocumentsTable.select { DocumentsTable.vacanteId eq vacanteId }
                .mapNotNull { mapRow(it) }
        }
    }

    fun findByType(type: String): List<Document> {
        return transaction {
            DocumentsTable.select { DocumentsTable.type eq type }
                .mapNotNull { mapRow(it) }
        }
    }

    private fun mapRow(row: ResultRow): Document {
        return Document(
            id = row[DocumentsTable.id],
            studentId = row[DocumentsTable.studentId],
            vacanteId = row[DocumentsTable.vacanteId],
            name = row[DocumentsTable.name],
            type = row[DocumentsTable.type],
            fileUrl = row[DocumentsTable.fileUrl],
            uploadedBy = row[DocumentsTable.uploadedBy],
            createdAt = row[DocumentsTable.createdAt]
        )
    }
}
