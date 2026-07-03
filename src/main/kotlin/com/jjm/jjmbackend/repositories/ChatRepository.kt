package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.ChatMessagesTable
import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.models.ChatMessage
import com.jjm.jjmbackend.dto.ChatResponse
import com.jjm.jjmbackend.dto.ConversationResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ChatRepository {

    fun send(senderId: Int, receiverId: Int, message: String, timestamp: String, vacanteId: Int?): ChatMessage? {
        return transaction {
            val insert = ChatMessagesTable.insert {
                it[ChatMessagesTable.senderId] = senderId
                it[ChatMessagesTable.receiverId] = receiverId
                it[ChatMessagesTable.message] = message
                it[ChatMessagesTable.timestamp] = timestamp
                it[ChatMessagesTable.vacanteId] = vacanteId
            }
            insert.resultedValues?.firstOrNull()?.let { mapRow(it) }
        }
    }

    fun getConversation(userId1: Int, userId2: Int): List<ChatResponse> {
        return transaction {
            (ChatMessagesTable.innerJoin(UsersTable, { ChatMessagesTable.senderId }, { UsersTable.id }))
                .select { (ChatMessagesTable.senderId eq userId1 and (ChatMessagesTable.receiverId eq userId2)) or
                         (ChatMessagesTable.senderId eq userId2 and (ChatMessagesTable.receiverId eq userId1)) }
                .orderBy(ChatMessagesTable.timestamp)
                .map { mapToResponse(it) }
        }
    }

    fun getConversations(userId: Int): List<ConversationResponse> {
        return transaction {
            val sentMessages = ChatMessagesTable.select {
                (ChatMessagesTable.senderId eq userId) or (ChatMessagesTable.receiverId eq userId)
            }.orderBy(ChatMessagesTable.timestamp, SortOrder.DESC)

            val otherUserIds = sentMessages.map { row ->
                if (row[ChatMessagesTable.senderId] == userId) row[ChatMessagesTable.receiverId]
                else row[ChatMessagesTable.senderId]
            }.distinct()

            otherUserIds.mapNotNull { otherUserId ->
                val lastMsg = ChatMessagesTable.select {
                    ((ChatMessagesTable.senderId eq userId) and (ChatMessagesTable.receiverId eq otherUserId)) or
                    ((ChatMessagesTable.senderId eq otherUserId) and (ChatMessagesTable.receiverId eq userId))
                }.orderBy(ChatMessagesTable.timestamp, SortOrder.DESC).limit(1).singleOrNull() ?: return@mapNotNull null

                val otherUser = UsersTable.select { UsersTable.id eq otherUserId }.singleOrNull() ?: return@mapNotNull null

                ConversationResponse(
                    userId = otherUserId,
                    userName = otherUser[UsersTable.name],
                    lastMessage = lastMsg[ChatMessagesTable.message],
                    lastTimestamp = lastMsg[ChatMessagesTable.timestamp]
                )
            }
        }
    }

    private fun mapRow(row: ResultRow): ChatMessage {
        return ChatMessage(
            id = row[ChatMessagesTable.id],
            senderId = row[ChatMessagesTable.senderId],
            receiverId = row[ChatMessagesTable.receiverId],
            message = row[ChatMessagesTable.message],
            timestamp = row[ChatMessagesTable.timestamp],
            vacanteId = row[ChatMessagesTable.vacanteId]
        )
    }

    private fun mapToResponse(row: ResultRow): ChatResponse {
        return ChatResponse(
            id = row[ChatMessagesTable.id],
            senderId = row[ChatMessagesTable.senderId],
            senderName = row[UsersTable.name],
            receiverId = row[ChatMessagesTable.receiverId],
            receiverName = "",
            message = row[ChatMessagesTable.message],
            timestamp = row[ChatMessagesTable.timestamp],
            vacanteId = row[ChatMessagesTable.vacanteId]
        )
    }
}
