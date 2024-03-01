package com.example.chatservice.api.chat.domain.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collation = "message")
class ChatMessage() {

    @MongoId
    var id: String? = null;
    var roomId: String? = null;
    var memberId: Long? = null;
    var payload: String? = null;
    var createdAt: Long = System.currentTimeMillis();

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(ChatMessage::id)
        private val toStringProperties = arrayOf(
            ChatMessage::id,
            ChatMessage::roomId,
            ChatMessage::memberId,
        )
    }
}