package com.jjm.jjmbackend.database

import com.jjm.jjmbackend.config.AppConfig
import com.jjm.jjmbackend.database.tables.UsersTable
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
            SchemaUtils.create(
                UsersTable
            )
        }
    }
}
