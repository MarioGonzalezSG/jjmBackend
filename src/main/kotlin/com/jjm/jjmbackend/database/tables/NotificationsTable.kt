package com.jjm.jjmbackend.database.tables

import org.jetbrains.exposed.sql.Table

object NotificationsTable : Table("notifications") {

    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val title = varchar("title", 255)
    val message = varchar("message", 500)
    val type = varchar("type", 50).default("SYSTEM")
    val isRead = bool("is_read").default(false)
    val relatedId = integer("related_id").nullable()
    val createdAt = varchar("created_at", 50)

    override val primaryKey = PrimaryKey(id)
}
