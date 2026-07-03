package com.jjm.jjmbackend.database

import com.jjm.jjmbackend.config.AppConfig
import com.jjm.jjmbackend.database.tables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = AppConfig.jdbcUrl
            username = AppConfig.dbUser
            password = AppConfig.dbPassword
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
        }

        val dataSource = HikariDataSource(config)

        Database.connect(dataSource)

        migrate()
    }

    private fun migrate() {
        transaction {
            // Crear tablas nuevas si no existen
            SchemaUtils.create(
                UsersTable,
                CompaniesTable,
                VacantesTable,
                PostulacionesTable,
                DocumentsTable,
                AttendanceTable,
                ChatMessagesTable,
                NotificationsTable
            )

            // Migrar columnas faltantes en tablas existentes
            try {
                exec("ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'ESTUDIANTE'")
            } catch (_: Exception) { }

            try {
                exec("ALTER TABLE companies ADD COLUMN IF NOT EXISTS description VARCHAR(500)")
                exec("ALTER TABLE companies ADD COLUMN IF NOT EXISTS address VARCHAR(255)")
                exec("ALTER TABLE companies ADD COLUMN IF NOT EXISTS phone VARCHAR(50)")
                exec("ALTER TABLE companies ADD COLUMN IF NOT EXISTS website VARCHAR(255)")
                exec("ALTER TABLE companies ADD COLUMN IF NOT EXISTS latitude DOUBLE PRECISION")
                exec("ALTER TABLE companies ADD COLUMN IF NOT EXISTS longitude DOUBLE PRECISION")
            } catch (_: Exception) { }

            try {
                exec("ALTER TABLE vacantes ADD COLUMN IF NOT EXISTS latitude DOUBLE PRECISION")
                exec("ALTER TABLE vacantes ADD COLUMN IF NOT EXISTS longitude DOUBLE PRECISION")
            } catch (_: Exception) { }
        }
    }
}
