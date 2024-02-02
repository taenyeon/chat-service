package com.example.chatservice.api.chat.repository

import com.example.chatservice.api.chat.domain.entity.ChatMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ChatMessageRepository : MongoRepository<ChatMessage, String> {
    fun findAllByRoomIdAndIssuedDateTimeBetween(
        roomId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): List<ChatMessage>

    fun findAllByRoomId(roomId: String): List<ChatMessage>

    fun findAllByRoomIdAndMemberId(roomId: String, memberId: Long): List<ChatMessage>
}