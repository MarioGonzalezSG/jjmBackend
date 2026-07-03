package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.dto.ChatRequest
import com.jjm.jjmbackend.dto.ChatResponse
import com.jjm.jjmbackend.dto.ConversationResponse
import com.jjm.jjmbackend.repositories.ChatRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatService(
    private val chatRepository: ChatRepository
) {
    fun send(senderId: Int, request: ChatRequest): Boolean {
        chatRepository.send(
            senderId = senderId,
            receiverId = request.receiverId,
            message = request.message,
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            vacanteId = request.vacanteId
        )
        return true
    }

    fun getConversation(userId1: Int, userId2: Int): List<ChatResponse> {
        return chatRepository.getConversation(userId1, userId2)
    }

    fun getConversations(userId: Int): List<ConversationResponse> {
        return chatRepository.getConversations(userId)
    }
}
