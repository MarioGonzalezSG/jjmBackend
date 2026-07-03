package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.NotificationsTable
import com.jjm.jjmbackend.models.Notification
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationRepository {

    fun findByUserId(userId: Int): List<Notification> {
        return transaction {
            NotificationsTable
                .select { NotificationsTable.userId eq userId }
                .orderBy(NotificationsTable.createdAt to SortOrder.DESC)
                .map { mapRow(it) }
        }
    }

    fun create(userId: Int, title: String, message: String, type: String = "SYSTEM", relatedId: Int? = null): Notification? {
        return transaction {
            val now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            NotificationsTable.insert {
                it[NotificationsTable.userId] = userId
                it[NotificationsTable.title] = title
                it[NotificationsTable.message] = message
                it[NotificationsTable.type] = type
                it[NotificationsTable.relatedId] = relatedId
                it[NotificationsTable.createdAt] = now
            }.resultedValues?.firstOrNull()?.let { mapRow(it) }
        }
    }

    fun markAsRead(notificationId: Int): Boolean {
        return transaction {
            NotificationsTable.update({ NotificationsTable.id eq notificationId }) {
                it[isRead] = true
            } > 0
        }
    }

    fun markAllAsRead(userId: Int) {
        transaction {
            NotificationsTable.update({ NotificationsTable.userId eq userId and NotificationsTable.isRead.eq(false) }) {
                it[isRead] = true
            }
        }
    }

    fun countUnread(userId: Int): Long {
        return transaction {
            NotificationsTable.select { NotificationsTable.userId eq userId and NotificationsTable.isRead.eq(false) }.count()
        }
    }

    private fun mapRow(row: ResultRow): Notification {
        return Notification(
            id = row[NotificationsTable.id],
            userId = row[NotificationsTable.userId],
            title = row[NotificationsTable.title],
            message = row[NotificationsTable.message],
            type = row[NotificationsTable.type],
            isRead = row[NotificationsTable.isRead],
            relatedId = row[NotificationsTable.relatedId],
            createdAt = row[NotificationsTable.createdAt]
        )
    }
}
