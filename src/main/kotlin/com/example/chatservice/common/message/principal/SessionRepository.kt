package com.example.chatservice.common.message.principal

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class SessionRepository {
    private val sessions = ConcurrentHashMap<Long, String>()

    fun select(userId: Long): String?{
        return sessions[userId]
    }

    fun selectAll(): ConcurrentHashMap<Long, String> {
        return sessions
    }
    fun enter(userId: Long, sessionId: String) {
        sessions[userId] = sessionId
    }

    fun out(sessionId: String) {
        sessions.forEach { (key, value) ->
            if (sessionId == value) sessions.remove(key)
        }
    }

    fun reset() = sessions.clear()
}