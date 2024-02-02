package com.example.chatservice.api.chat.repository.support

import com.example.chatservice.api.chat.domain.entity.QChatMember.chatMember
import com.example.chatservice.api.chat.domain.entity.QChatRoom.chatRoom
import com.example.chatservice.api.chat.repository.support.interfaces.ChatMemberSupport
import com.example.chatservice.api.member.domain.entity.Member
import com.example.chatservice.api.member.domain.entity.QMember.member
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ChatMemberSupportImpl(
    private val queryFactory: JPAQueryFactory,
) : ChatMemberSupport {
    override fun findAllMemberByRoomId(roomId: String): MutableList<Member> {
        return queryFactory
            .select(member)
            .from(member)
            .leftJoin(chatMember)
            .on(chatMember.memberId.eq(member.id))
            .where(chatMember.roomId.eq(roomId))
            .fetch()
    }

    override fun findMemberByMemberIdAndRoomId(memberId: Long, roomId: String): Member? {
        return queryFactory
            .select(member)
            .from(chatMember)
            .leftJoin(member)
            .on(chatMember.memberId.eq(member.id))
            .where(
                chatMember.memberId.eq(memberId)
                    .and(chatMember.roomId.eq(roomId))
            )
            .fetchFirst()
    }
}