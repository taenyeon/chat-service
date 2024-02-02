package com.example.chatservice.api.member.domain.mapper

import com.example.chatservice.api.member.domain.dto.MemberDto
import com.example.chatservice.api.member.domain.entity.Member
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import com.example.chatservice.common.encrypt.annotation.Decrypt
import com.example.chatservice.common.encrypt.annotation.Encrypt
import com.example.chatservice.common.interfaces.EntityMapper
import com.example.chatservice.common.util.EncryptUtil

@Mapper(uses = [EncryptUtil::class])
interface MemberDtoMapper : EntityMapper<Member, MemberDto> {

    @Mapping(source = "username", target = "username", qualifiedBy = [Encrypt::class])
    @Mapping(source = "name", target = "name", qualifiedBy = [Encrypt::class])
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedBy = [Encrypt::class])
    override fun toEntity(dto: MemberDto): Member

    @Mapping(source = "username", target = "username", qualifiedBy = [Decrypt::class])
    @Mapping(source = "name", target = "name", qualifiedBy = [Decrypt::class])
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedBy = [Decrypt::class])
    override fun toDto(entity: Member): MemberDto

}