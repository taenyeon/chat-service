package com.example.chatservice.api.chat.service

import com.example.chatservice.api.chat.domain.entity.ChatMessage
import com.example.chatservice.api.chat.repository.ChatMessageRepository
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.http.constant.ResponseCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository
) {
    fun add(chatMessage: ChatMessage) {
        chatMessageRepository.save(chatMessage)
    }

    fun select(_id: String): ChatMessage {
        return chatMessageRepository.findByIdOrNull(_id)
            ?: throw ResponseException(ResponseCode.NOT_FOUND_ERROR)
    }

    fun selectList(roomId: String): List<ChatMessage> {
        return chatMessageRepository.findAllByRoomId(roomId)
    }

    fun selectList(roomId: String, memberId: Long): List<ChatMessage> {
        return chatMessageRepository.findAllByRoomIdAndMemberId(roomId, memberId)
    }

    fun selectList(roomId: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<ChatMessage>? {
        return null
    }
}