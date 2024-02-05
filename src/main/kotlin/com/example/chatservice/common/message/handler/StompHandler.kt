package com.example.chatservice.common.message.handler

import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.function.logger
import com.example.chatservice.common.http.constant.ResponseCode
import com.example.chatservice.common.message.principal.SessionRepository
import com.example.chatservice.common.security.provider.JwtTokenProvider
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class StompHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val sessionRepository: SessionRepository
) : ChannelInterceptor {

    val log = logger()


    @Override
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor: StompHeaderAccessor = StompHeaderAccessor.wrap(message)
        val accessToken =
            accessor.getFirstNativeHeader("access_token")
        val sessionId = accessor.user?.name
        when (accessor.command) {
            StompCommand.CONNECT -> {
                log.info("ACCESS_TOKEN : $accessToken")
                accessToken ?: throw ResponseException(ResponseCode.INVALID_TOKEN)
                val userId = jwtTokenProvider.parseIdFromJWT(accessToken)
                sessionRepository.enter(userId, sessionId.toString())
                log.info("[STOMP] RECEIVE MESSAGE - COMMAND : ${accessor.command}, USER-ID : $userId, SESSION-ID : $sessionId")
            }

            StompCommand.SUBSCRIBE -> {
            }

            StompCommand.DISCONNECT -> {
                sessionRepository.out(sessionId.toString())
            }

            else -> {}
        }
        accessorLogging(accessor.command!!, accessToken, sessionId)
        return message
    }

    fun accessorLogging(command: StompCommand, accessToken: String?, sessionId: String?) {
        log.info("[STOMP] RECEIVE MESSAGE - COMMAND : ${command}, ACCESS_TOKEN : $accessToken, SESSION-ID : $sessionId")
    }
}