package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    fun create(email: String, password: String, name: String, role: String): User? {
        return transaction {
            val insertStatement = UsersTable.insert {
                it[UsersTable.email] = email
                it[UsersTable.password] = password
                it[UsersTable.name] = name
                it[UsersTable.role] = role
            }
            insertStatement.resultedValues?.firstOrNull()?.let { mapRowToUser(it) }
        }
    }

    fun findByEmailRaw(email: String): ResultRow? {
        return transaction {
            UsersTable.select { UsersTable.email eq email }.singleOrNull()
        }
    }

    fun findModelByEmail(email: String): User? {
        return transaction {
            UsersTable.select { UsersTable.email eq email }.mapNotNull { mapRowToUser(it) }.singleOrNull()
        }
    }

    fun findById(id: Int): User? {
        return transaction {
            UsersTable.select { UsersTable.id eq id }.mapNotNull { mapRowToUser(it) }.singleOrNull()
        }
    }

    private fun mapRowToUser(row: ResultRow): User {
        return User(
            id = row[UsersTable.id],
            email = row[UsersTable.email],
            name = row[UsersTable.name],
            role = row[UsersTable.role]
        )
    }
}
