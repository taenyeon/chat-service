package com.example.chatservice.api.member.service

import com.example.chatservice.api.member.domain.entity.Member
import com.example.chatservice.api.member.repository.MemberRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.http.constant.ResponseCode

@Service
class MemberCacheService(
    private val memberRepository: MemberRepository
) {

    @Cacheable(value = ["member"], key = "#id")
    fun findMember(id: Long): Member {
        return memberRepository.findById(id)
            .orElseThrow { ResponseException(ResponseCode.NOT_FOUND_ERROR) }
    }
}