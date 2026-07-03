package com.jjm.jjmbackend.database.tables

import org.jetbrains.exposed.sql.Table

object DocumentsTable : Table("documents") {
    val id = integer("id").autoIncrement()
    val studentId = integer("student_id").references(UsersTable.id).nullable()
    val vacanteId = integer("vacante_id").references(VacantesTable.id).nullable()
    val name = varchar("name", 255)
    val type = varchar("type", 50) // ESTUDIANTE, EMPRESA, OFICIAL
    val fileUrl = varchar("file_url", 500)
    val uploadedBy = integer("uploaded_by").references(UsersTable.id)
    val createdAt = varchar("created_at", 50)

    override val primaryKey = PrimaryKey(id)
}
