package com.example.chatservice.api.chat.service

import com.example.chatservice.api.chat.domain.entity.ChatRoom
import com.example.chatservice.api.chat.repository.ChatMemberRepository
import com.example.chatservice.api.chat.repository.ChatRoomRepository
import com.example.chatservice.api.chat.repository.support.ChatMemberSupportImpl
import com.example.chatservice.api.member.domain.entity.Member
import com.example.chatservice.api.member.repository.MemberRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.function.logger
import com.example.chatservice.common.http.constant.ResponseCode
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.repository.findByIdOrNull

@Service
class ChatMemberCacheService(
    private val chatMemberSupport: ChatMemberSupportImpl,
) {
    val log = logger()
    @Cacheable(value = ["chatMember"], key = "#roomId")
    fun selectList(roomId: String): MutableList<Member> {
        return chatMemberSupport.findAllMemberByRoomId(roomId)
    }

    @CacheEvict(value = ["chatMember"], key = "#roomId")
    fun delete(roomId: String) {
        log.info("[REDIS] DELETE - chatMember::$roomId")
    }

}