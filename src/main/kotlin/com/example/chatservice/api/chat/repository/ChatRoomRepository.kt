package com.example.chatservice.api.chat.repository

import com.example.chatservice.api.chat.domain.entity.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, String> {
    fun findAllByHostId(hostId : Long): MutableList<ChatRoom>
}