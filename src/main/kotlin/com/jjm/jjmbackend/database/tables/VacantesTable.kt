package com.jjm.jjmbackend.database.tables

import org.jetbrains.exposed.sql.Table

object VacantesTable : Table("vacantes") {
    val id = integer("id").autoIncrement()
    val companyId = integer("company_id").references(UsersTable.id)
    val title = varchar("title", 200)
    val description = varchar("description", 2000)
    val requirements = varchar("requirements", 1000).nullable()
    val slots = integer("slots")
    val area = varchar("area", 100).nullable()
    val duration = varchar("duration", 100).nullable()
    val schedule = varchar("schedule", 200).nullable()
    val location = varchar("location", 255).nullable()
    val status = varchar("status", 20).default("ACTIVA") // ACTIVA, CERRADA
    val createdAt = varchar("created_at", 50)

    override val primaryKey = PrimaryKey(id)
}
