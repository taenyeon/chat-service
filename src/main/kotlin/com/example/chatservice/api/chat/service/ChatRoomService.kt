package com.example.chatservice.api.chat.service

import com.example.chatservice.api.chat.domain.entity.ChatMember
import com.example.chatservice.api.chat.domain.entity.ChatRoom
import com.example.chatservice.api.chat.repository.ChatMemberRepository
import com.example.chatservice.api.chat.repository.ChatRoomRepository
import com.example.chatservice.api.chat.repository.support.ChatRoomSupportImpl
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.http.constant.ResponseCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMemberRepository: ChatMemberRepository,
    private val chatRoomSupport: ChatRoomSupportImpl
) {

    fun select(roomId: String): ChatRoom {
        return chatRoomRepository.findByIdOrNull(roomId)
            ?: throw ResponseException(ResponseCode.NOT_FOUND_ERROR)
    }

    fun select(memberId: Long, roomId: String): ChatRoom {
        return chatRoomSupport.findByMemberIdAndRoomId(memberId, roomId)
            ?: throw ResponseException(ResponseCode.NOT_FOUND_ERROR)
    }

    fun selectList(memberId: Long): MutableList<ChatRoom> {
        return chatRoomSupport.findByMemberId(memberId)
    }

    fun selectListByHost(userId: Long): MutableList<ChatRoom> {
        return chatRoomRepository.findAllByHostId(userId)
    }


    fun add(userId: Long, name: String): String {
        // create room
        val roomId = UUID.randomUUID().toString()
        val chatRoom = ChatRoom(roomId, name, userId)
        chatRoomRepository.save(chatRoom)

        //  init member
        val chatMember = ChatMember(null, userId, roomId)
        chatMemberRepository.save(chatMember)
        return roomId
    }

    fun add(userId: Long, name: String, memberIds: MutableList<Long>): String {
        val roomId = add(userId, name)
        memberIds.forEach { memberId: Long ->
            chatMemberRepository.save(ChatMember(null, memberId, roomId))
        }
        return roomId
    }

    fun delete(userId: Long, roomId: String) {
        val chatRoom = select(roomId)
        checkOwn(userId, chatRoom.hostId!!)
        chatRoomRepository.delete(chatRoom)
    }

    fun checkOwn(userId: Long, id: Long) {
        if (userId != id) throw ResponseException(ResponseCode.ACCESS_DENIED_ERROR)
    }

}