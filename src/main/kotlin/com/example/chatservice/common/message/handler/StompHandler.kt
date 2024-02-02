package com.example.chatservice.common.message.handler

import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.function.logger
import com.example.chatservice.common.http.constant.ResponseCode
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class StompHandler() : ChannelInterceptor {
    val log = logger()
    @Override
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor: StompHeaderAccessor = StompHeaderAccessor.wrap(message)
        when(accessor.command){
            StompCommand.CONNECT -> {
                val accessToken = accessor.getFirstNativeHeader("access_token")
                log.info("[STOMP] RECEIVE MESSAGE - COMMAND : ${accessor.command}, ACCESS_TOKEN : $accessToken")
            }
            StompCommand.SUBSCRIBE -> {
                val accessToken = accessor.getFirstNativeHeader("access_token")
                log.info("[STOMP] RECEIVE MESSAGE - COMMAND : ${accessor.command}, ACCESS_TOKEN : $accessToken")
            }
            StompCommand.DISCONNECT -> {
                val accessToken = accessor.getFirstNativeHeader("access_token")
                log.info("[STOMP] RECEIVE MESSAGE - COMMAND : ${accessor.command}, ACCESS_TOKEN : $accessToken")
            }
            else -> {}
        }
        return message
    }
}