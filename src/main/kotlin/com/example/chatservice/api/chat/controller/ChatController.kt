package com.example.chatservice.api.chat.controller

import com.example.chatservice.api.chat.domain.entity.ChatMessage
import com.example.chatservice.api.chat.service.ChatService
import com.example.chatservice.common.function.logger
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val chatService: ChatService
) {

    @MessageMapping(value = ["/chat/message"])
    fun sendMessage(
        @RequestBody message: ChatMessage,
        @Header(name = "access_token", required = true) accessToken: String
    ) {
        logger().info("accessToken : $accessToken")
        chatService.send(message, accessToken)
    }


}