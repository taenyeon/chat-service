package com.example.chatservice.api.chat.service

import com.example.chatservice.api.chat.domain.entity.ChatRoom
import com.example.chatservice.api.chat.repository.ChatRoomRepository
import com.example.chatservice.api.member.domain.entity.Member
import com.example.chatservice.api.member.repository.MemberRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.http.constant.ResponseCode
import org.springframework.data.repository.findByIdOrNull

@Service
class ChatRoomCacheService(
    private val chatRoomRepository: ChatRoomRepository
) {

    @Cacheable(value = ["chatRoom"], key = "#id")
    fun select(id: String): ChatRoom {
        return chatRoomRepository.findByIdOrNull(id)
            ?: throw ResponseException(ResponseCode.NOT_FOUND_ERROR)
    }

}