package com.jjm.jjmbackend.database.tables

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {

    val id = integer("id").autoIncrement()
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val name = varchar("name", 100)
    val role = varchar("role", 20) // ESTUDIANTE, EMPRESA, ADMINISTRADOR

    override val primaryKey = PrimaryKey(id)
}
