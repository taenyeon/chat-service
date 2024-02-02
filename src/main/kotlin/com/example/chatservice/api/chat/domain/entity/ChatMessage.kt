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
    @MongoId
    var _id: String?,
    var roomId: String?,
    var memberId: Long?,
    var payload: String?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var issuedDateTime: LocalDateTime

) {
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