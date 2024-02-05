package com.example.chatservice.api.chat.service

import com.example.chatservice.api.chat.domain.entity.ChatMessage
import com.example.chatservice.api.member.service.MemberCacheService
import com.example.chatservice.common.message.kafka.Producer
import com.example.chatservice.common.security.provider.JwtTokenProvider
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val producer: Producer,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberCacheService: MemberCacheService
) {

    fun send(message: ChatMessage, accessToken: String) {
        message.memberId = jwtTokenProvider.parseIdFromJWT(accessToken)
        producer.sendMessage(message)
    }

    fun enter(message: ChatMessage, memberId: Long) {
        val memberName = memberCacheService.findMember(memberId).name
        message.memberId = memberId
        message.payload = "$memberName 님이 채팅방에 참여하였습니다."
        producer.sendMessage(message)
    }

    fun out(message: ChatMessage, memberId: Long) {
        val memberName = memberCacheService.findMember(memberId).name
        message.memberId = memberId
        message.payload = "$memberName 님이 채팅방에서 나갔습니다."
        producer.sendMessage(message)
    }
}