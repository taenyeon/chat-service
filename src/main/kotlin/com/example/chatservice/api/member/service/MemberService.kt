package com.example.chatservice.api.member.service

import com.example.chatservice.api.member.domain.mapper.MemberDtoMapper
import com.example.chatservice.api.member.domain.dto.MemberDto
import com.example.chatservice.api.member.domain.entity.Member
import com.example.chatservice.api.member.repository.MemberRepository
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.function.encrypt
import com.example.chatservice.common.function.logger
import com.example.chatservice.common.http.constant.ResponseCode
import org.springframework.data.repository.findByIdOrNull

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {
    val log = logger()

    // mapStruct
    private val memberDtoMapper: MemberDtoMapper = Mappers.getMapper(MemberDtoMapper::class.java)


    // Entity
    fun addMember(member: Member) {
        checkExistMember(member.username!!)
        memberRepository.save(member)
    }

    fun deleteMember(id: Long) {
        try {
            memberRepository.deleteById(id)
        } catch (e: Exception) {
            throw ResponseException(ResponseCode.NOT_FOUND_ERROR, e)
        }
    }


    fun findMember(username: String): Member {
        log.info("username : $username")
        return memberRepository.findByUsername(username.encrypt())
            ?: throw ResponseException(ResponseCode.NOT_FOUND_ERROR)
    }

    fun checkExistMember(username: String) {
        if (memberRepository.findByUsername(username.encrypt()) != null) {
            throw ResponseException(ResponseCode.EXIST_MEMBER)
        }
    }

    fun findMember(id: Long): Member {
        return memberRepository.findByIdOrNull(id)
            ?: throw ResponseException(ResponseCode.NOT_FOUND_ERROR)
    }

    // Dto
    fun findMemberDto(username: String): MemberDto {
        return memberDtoMapper.toDto(findMember(username))
    }

    fun findMemberDto(id: Long): MemberDto {
        return memberDtoMapper.toDto(findMember(id))
    }
}