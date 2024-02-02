package com.example.chatservice.api.member.repository

import com.example.chatservice.api.chat.repository.support.interfaces.ChatMemberSupport
import com.example.chatservice.api.member.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findByUsername(username: String?): Member?

}