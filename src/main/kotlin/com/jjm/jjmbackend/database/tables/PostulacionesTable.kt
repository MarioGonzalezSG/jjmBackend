package com.jjm.jjmbackend.database.tables

import org.jetbrains.exposed.sql.Table

object PostulacionesTable : Table("postulaciones") {
    val id = integer("id").autoIncrement()
    val studentId = integer("student_id").references(UsersTable.id)
    val vacanteId = integer("vacante_id").references(VacantesTable.id)
    val status = varchar("status", 20).default("PENDIENTE") // PENDIENTE, ACEPTADA, RECHAZADA
    val message = varchar("message", 500).nullable()
    val createdAt = varchar("created_at", 50)

    override val primaryKey = PrimaryKey(id)
}
