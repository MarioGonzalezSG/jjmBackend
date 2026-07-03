package com.jjm.jjmbackend.database.tables

import org.jetbrains.exposed.sql.Table

object AttendanceTable : Table("attendance") {
    val id = integer("id").autoIncrement()
    val studentId = integer("student_id").references(UsersTable.id)
    val companyId = integer("company_id").references(UsersTable.id)
    val date = varchar("date", 20)
    val status = varchar("status", 20) // PRESENTE, FALTA, JUSTIFICADO
    val notes = varchar("notes", 500).nullable()

    override val primaryKey = PrimaryKey(id)
}
