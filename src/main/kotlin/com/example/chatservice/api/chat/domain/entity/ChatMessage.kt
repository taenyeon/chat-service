package com.example.chatservice.api.chat.domain.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.LocalDateTime

@Document(collation = "message")
class ChatMessage(
) {
    @MongoId
    var _id: String? = null
    var roomId: String? = null
    var memberId: Long? = null
    var payload: String? = null

    var createdAt: Int = 0

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(ChatMessage::_id)
        private val toStringProperties = arrayOf(
            ChatMessage::_id,
            ChatMessage::roomId,
            ChatMessage::memberId,
        )
    }
}