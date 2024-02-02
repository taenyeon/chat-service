package com.example.chatservice.api.chat.repository.support.interfaces

import com.example.chatservice.api.chat.domain.entity.ChatRoom

interface ChatRoomSupport{
    fun findByMemberId(memberId: Long): MutableList<ChatRoom>
    fun findByMemberIdAndRoomId(memberId:Long, roomId:String): ChatRoom?

}