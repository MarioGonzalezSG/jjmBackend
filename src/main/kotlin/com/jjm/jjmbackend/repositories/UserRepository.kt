package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    // 🔹 CREAR USUARIO
    fun create(email: String, password: String, name: String): User? {
        return transaction {

            val insertStatement = UsersTable.insert {
                it[UsersTable.email] = email
                it[UsersTable.password] = password
                it[UsersTable.name] = name
            }

            insertStatement.resultedValues?.firstOrNull()?.let {
                mapRowToUser(it)
            }
        }
    }

    // 🔹 BUSCAR POR EMAIL (RAW ROW - útil para login)
    fun findByEmailRaw(email: String): ResultRow? {
        return transaction {
            UsersTable
                .select { UsersTable.email eq email }
                .singleOrNull()
        }
    }

    // 🔹 BUSCAR USER (MODEL limpio)
    fun findModelByEmail(email: String): User? {
        return transaction {
            UsersTable
                .select { UsersTable.email eq email }
                .mapNotNull { mapRowToUser(it) }
                .singleOrNull()
        }
    }

    // 🔹 BUSCAR POR ID (opcional pero útil)
    fun findById(id: Int): User? {
        return transaction {
            UsersTable
                .select { UsersTable.id eq id }
                .mapNotNull { mapRowToUser(it) }
                .singleOrNull()
        }
    }

    // 🔹 MAPEO CENTRALIZADO (IMPORTANTE)
    private fun mapRowToUser(row: ResultRow): User {
        return User(
            id = row[UsersTable.id],
            email = row[UsersTable.email],
            name = row[UsersTable.name]
        )
    }
}