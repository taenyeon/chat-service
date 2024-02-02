package com.example.chatservice.api.chat.repository.support.interfaces

import com.example.chatservice.api.member.domain.entity.Member
import org.springframework.stereotype.Repository

interface ChatMemberSupport {
    fun findAllMemberByRoomId(roomId: String): MutableList<Member>
    fun findMemberByMemberIdAndRoomId(memberId:Long, roomId: String): Member?
}