package com.example.chatservice.api.chat.repository.support

import com.example.chatservice.api.chat.domain.entity.ChatRoom
import com.example.chatservice.api.chat.domain.entity.QChatMember.chatMember
import com.example.chatservice.api.chat.domain.entity.QChatRoom.chatRoom
import com.example.chatservice.api.chat.repository.support.interfaces.ChatRoomSupport
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
class ChatRoomSupportImpl(
    private val queryFactory: JPAQueryFactory,
) : ChatRoomSupport {
    override fun findByMemberId(memberId: Long): MutableList<ChatRoom> {
        return queryFactory
            .select(chatRoom)
            .from(chatMember)
            .leftJoin(chatRoom)
            .on(chatRoom.id.eq(chatMember.roomId))
            .where(chatMember.memberId.eq(memberId))
            .fetch()
    }

    override fun findByMemberIdAndRoomId(memberId: Long, roomId: String): ChatRoom? {
        return queryFactory
            .select(chatRoom)
            .from(chatMember)
            .leftJoin(chatRoom)
            .on(chatRoom.id.eq(chatMember.roomId))
            .where(
                chatMember.memberId.eq(memberId)
                    .and(chatMember.roomId.eq(roomId))
            )
            .fetchFirst()
    }
}