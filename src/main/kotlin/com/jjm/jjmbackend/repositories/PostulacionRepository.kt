package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.PostulacionesTable
import com.jjm.jjmbackend.database.tables.VacantesTable
import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.models.Postulacion
import com.jjm.jjmbackend.dto.PostulacionResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PostulacionRepository {

    fun create(studentId: Int, vacanteId: Int, message: String?, createdAt: String): PostulacionResponse? {
        return transaction {
            val insert = PostulacionesTable.insert {
                it[PostulacionesTable.studentId] = studentId
                it[PostulacionesTable.vacanteId] = vacanteId
                it[PostulacionesTable.message] = message
                it[PostulacionesTable.createdAt] = createdAt
            }
            val id = insert.resultedValues?.firstOrNull()?.get(PostulacionesTable.id) ?: return@transaction null
            (PostulacionesTable
                .innerJoin(UsersTable, { PostulacionesTable.studentId }, { UsersTable.id })
                .innerJoin(VacantesTable, { PostulacionesTable.vacanteId }, { VacantesTable.id }))
                .select { PostulacionesTable.id eq id }
                .mapNotNull { mapToResponse(it) }
                .singleOrNull()
        }
    }

    fun findByStudent(studentId: Int): List<PostulacionResponse> {
        return transaction {
            (PostulacionesTable
                .innerJoin(UsersTable, { PostulacionesTable.studentId }, { UsersTable.id })
                .innerJoin(VacantesTable, { PostulacionesTable.vacanteId }, { VacantesTable.id }))
                .select { PostulacionesTable.studentId eq studentId }
                .map { mapToResponse(it) }
        }
    }

    fun findByVacante(vacanteId: Int): List<PostulacionResponse> {
        return transaction {
            (PostulacionesTable
                .innerJoin(UsersTable, { PostulacionesTable.studentId }, { UsersTable.id })
                .innerJoin(VacantesTable, { PostulacionesTable.vacanteId }, { VacantesTable.id }))
                .select { PostulacionesTable.vacanteId eq vacanteId }
                .map { mapToResponse(it) }
        }
    }

    fun updateStatus(id: Int, status: String): Boolean {
        return transaction {
            PostulacionesTable.update({ PostulacionesTable.id eq id }) {
                it[PostulacionesTable.status] = status
            } > 0
        }
    }

    fun findById(id: Int): Postulacion? {
        return transaction {
            PostulacionesTable.select { PostulacionesTable.id eq id }
                .mapNotNull { mapRow(it) }
                .singleOrNull()
        }
    }

    fun findByStudentAndVacante(studentId: Int, vacanteId: Int): Postulacion? {
        return transaction {
            PostulacionesTable.select {
                (PostulacionesTable.studentId eq studentId) and (PostulacionesTable.vacanteId eq vacanteId)
            }.mapNotNull { mapRow(it) }.singleOrNull()
        }
    }

    fun findAcceptedByCompany(companyId: Int): List<PostulacionResponse> {
        return transaction {
            (PostulacionesTable
                .innerJoin(UsersTable, { PostulacionesTable.studentId }, { UsersTable.id })
                .innerJoin(VacantesTable, { PostulacionesTable.vacanteId }, { VacantesTable.id }))
                .select { (VacantesTable.companyId eq companyId) and (PostulacionesTable.status eq "ACEPTADA") }
                .map { mapToResponse(it) }
        }
    }

    private fun mapRow(row: ResultRow): Postulacion {
        return Postulacion(
            id = row[PostulacionesTable.id],
            studentId = row[PostulacionesTable.studentId],
            vacanteId = row[PostulacionesTable.vacanteId],
            status = row[PostulacionesTable.status],
            message = row[PostulacionesTable.message],
            createdAt = row[PostulacionesTable.createdAt]
        )
    }

    private fun mapToResponse(row: ResultRow): PostulacionResponse {
        return PostulacionResponse(
            id = row[PostulacionesTable.id],
            studentId = row[PostulacionesTable.studentId],
            studentName = row[UsersTable.name],
            studentEmail = row[UsersTable.email],
            vacanteId = row[PostulacionesTable.vacanteId],
            vacanteTitle = row[VacantesTable.title],
            status = row[PostulacionesTable.status],
            message = row[PostulacionesTable.message],
            createdAt = row[PostulacionesTable.createdAt]
        )
    }
}
