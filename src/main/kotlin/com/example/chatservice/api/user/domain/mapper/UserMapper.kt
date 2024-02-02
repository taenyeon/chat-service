package com.example.chatservice.api.user.domain.mapper

import com.example.chatservice.api.member.domain.dto.MemberDto
import com.example.chatservice.api.member.domain.entity.Member
import com.example.chatservice.api.user.domain.dto.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import com.example.chatservice.common.encrypt.annotation.Decrypt
import com.example.chatservice.common.encrypt.annotation.Encrypt
import com.example.chatservice.common.util.EncryptUtil

@Mapper(uses = [EncryptUtil::class])
interface UserMapper {
    @Mapping(source = "username", target = "username", qualifiedBy = [Encrypt::class])
    @Mapping(source = "name", target = "name", qualifiedBy = [Encrypt::class])
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedBy = [Encrypt::class])
    @Mapping(target = "password", ignore = true)
    fun toMember(dto: User): Member

    @Mapping(source = "username", target = "username", qualifiedBy = [Decrypt::class])
    @Mapping(source = "name", target = "name", qualifiedBy = [Decrypt::class])
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedBy = [Decrypt::class])
    fun toUser(entity: Member): User

    fun toMemberResponse(dto: User): MemberDto
}