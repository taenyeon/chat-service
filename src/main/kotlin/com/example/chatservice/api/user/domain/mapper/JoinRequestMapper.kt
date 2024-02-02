package com.example.chatservice.api.user.domain.mapper

import com.example.chatservice.api.user.domain.dto.JoinRequest
import com.example.chatservice.api.member.domain.entity.Member
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import com.example.chatservice.common.encrypt.annotation.Encrypt
import com.example.chatservice.common.encrypt.annotation.PasswordEncrypt
import com.example.chatservice.common.interfaces.EntityMapper
import com.example.chatservice.common.util.EncryptUtil

@Mapper(uses = [EncryptUtil::class])
interface JoinRequestMapper : EntityMapper<Member, JoinRequest> {

    @Mapping(source = "password", target = "password", qualifiedBy = [PasswordEncrypt::class])
    @Mapping(source = "username", target = "username", qualifiedBy = [Encrypt::class])
    @Mapping(source = "name", target = "name", qualifiedBy = [Encrypt::class])
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedBy = [Encrypt::class])
    override fun toEntity(dto: JoinRequest): Member

    override fun toDto(entity: Member): JoinRequest
}