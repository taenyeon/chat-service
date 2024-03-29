package com.example.chatservice.common.message.kafka

import com.example.chatservice.api.chat.domain.entity.ChatMessage
import com.example.chatservice.api.chat.service.ChatMemberCacheService
import com.example.chatservice.common.function.logger
import com.example.chatservice.common.message.principal.SessionRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class Consumer(
    private val template: SimpMessagingTemplate,
    private val chatMemberCacheService: ChatMemberCacheService,
    private val sessionRepository: SessionRepository,
) {
    val log = logger()

    @KafkaListener(topics = ["chat"], groupId = "chat-service")
    fun receiveMessage(chatMessage: ChatMessage) {
        val members = chatMemberCacheService.selectList(chatMessage.roomId!!)
        log.info("[KAFKA - Consumer] RECEIVE - chatMessage : $chatMessage, target : $members")
        // todo async 로 할지?
        members.forEach { member ->
            val sessionId = sessionRepository.select(member.id!!)
            log.info("[STOMP] SEND MESSAGE - id : ${member.id}  SESSION-ID : $sessionId")
            if (sessionId != null) template.convertAndSend(
                "/sub/chat/$sessionId",
                chatMessage,
            )
        }
    }

}