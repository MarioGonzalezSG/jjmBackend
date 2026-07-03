package com.jjm.jjmbackend.database.tables

import org.jetbrains.exposed.sql.Table

object ChatMessagesTable : Table("chat_messages") {
    val id = integer("id").autoIncrement()
    val senderId = integer("sender_id").references(UsersTable.id)
    val receiverId = integer("receiver_id").references(UsersTable.id)
    val message = varchar("message", 1000)
    val timestamp = varchar("timestamp", 50)
    val vacanteId = integer("vacante_id").references(VacantesTable.id).nullable()

    override val primaryKey = PrimaryKey(id)
}
