package com.example.chatservice.api.chat.repository

import com.example.chatservice.api.chat.domain.entity.ChatMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMemberRepository : JpaRepository<ChatMember, String> {
    fun findByMemberIdAndRoomId(memberId:Long, roomId:String) : ChatMember?
}