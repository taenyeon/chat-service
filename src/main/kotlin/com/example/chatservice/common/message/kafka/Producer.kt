package com.example.chatservice.common.message.kafka

import com.example.chatservice.api.chat.domain.entity.ChatMessage
import com.example.chatservice.common.function.logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class Producer(
    private val kafkaTemplate: KafkaTemplate<String, ChatMessage>,
) {

    val log = logger()
    private val TOPIC = "chat"
    fun sendMessage(chatMessage: ChatMessage) {
        log.info("[KAFKA - PRODUCER] SEND - chatMessage : $chatMessage")
        kafkaTemplate.send(TOPIC, chatMessage)
    }

}