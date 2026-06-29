package com.jjm.jjmbackend.config

import io.github.cdimascio.dotenv.dotenv

object AppConfig {

    private val dotenv = dotenv {
        ignoreIfMissing = true
    }

    private fun get(name: String): String {
        return System.getenv(name)
            ?: dotenv[name]
            ?: throw IllegalStateException("La variable '$name' no está definida.")
    }

    val dbHost get() = get("PGHOST")
    val dbPort get() = get("PGPORT")
    val dbName get() = get("PGDATABASE")
    val dbUser get() = get("PGUSER")
    val dbPassword get() = get("PGPASSWORD")

    val jdbcUrl: String
        get() = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
}