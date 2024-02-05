package com.example.chatservice.common.message.handler

import com.example.chatservice.common.message.principal.ChatUser
import org.springframework.http.server.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal
import java.util.UUID

class JwtPrincipalHandshakeHandler(
) : DefaultHandshakeHandler() {
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal {
        val sessionId = UUID.randomUUID()
        return ChatUser(sessionId.toString())
    }
}