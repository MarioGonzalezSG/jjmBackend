package com.jjm.jjmbackend.database.tables

import org.jetbrains.exposed.sql.Table

object CompaniesTable : Table("companies") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").uniqueIndex().references(UsersTable.id)
    val description = varchar("description", 500).nullable()
    val address = varchar("address", 255).nullable()
    val phone = varchar("phone", 50).nullable()
    val website = varchar("website", 255).nullable()
    val latitude = double("latitude").nullable()
    val longitude = double("longitude").nullable()

    override val primaryKey = PrimaryKey(id)
}
