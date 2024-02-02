package com.example.chatservice.api.user.service

import com.example.chatservice.api.member.domain.dto.MemberDto
import com.example.chatservice.api.member.service.MemberService
import com.example.chatservice.api.member.domain.entity.Member
import com.example.chatservice.api.user.domain.dto.JwtToken
import com.example.chatservice.api.user.domain.dto.LoginRequest
import com.example.chatservice.api.user.domain.mapper.UserMapper
import org.mapstruct.factory.Mappers
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import com.example.chatservice.api.user.domain.dto.JoinRequest
import com.example.chatservice.api.user.domain.dto.User
import com.example.chatservice.api.user.domain.mapper.JoinRequestMapper
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.http.constant.ResponseCode
import com.example.chatservice.common.security.constant.TokenStatus
import com.example.chatservice.common.security.provider.JwtTokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Service
class UserService(
    private val memberService: MemberService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    private val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    private val joinRequestMapper: JoinRequestMapper = Mappers.getMapper(JoinRequestMapper::class.java)

    fun getUser(): MemberDto {
        return userMapper.toMemberResponse(SecurityContextHolder.getContext().authentication.details as User)
    }

    fun join(joinRequest: JoinRequest) {
        memberService.checkExistMember(joinRequest.username)
        val member = joinRequestMapper.toEntity(joinRequest)
        memberService.addMember(member)
    }

    fun login(loginRequest: LoginRequest): JwtToken {
        val member: Member = memberService.findMember(loginRequest.username)
        matchingPassword(loginRequest.password, member.password!!)
        return jwtTokenProvider.generateToken(member.id!!)
    }

    fun logout(id: Long) {
        jwtTokenProvider.dropRefreshToken(id)
    }

    fun reIssueAccessToken(refreshToken: String): String {
        when (jwtTokenProvider.validateToken(refreshToken)) {
            TokenStatus.ALLOW -> {
                val id = jwtTokenProvider.parseIdFromJWT(refreshToken)
                return jwtTokenProvider.generateAccessToken(id, now = Date())
            }

            else -> throw ResponseException(ResponseCode.INVALID_TOKEN)
        }
    }

    fun matchingPassword(rawPassword: String, encodedPassword: String) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw ResponseException(ResponseCode.WRONG_PASSWORD_ERROR)
    }
}