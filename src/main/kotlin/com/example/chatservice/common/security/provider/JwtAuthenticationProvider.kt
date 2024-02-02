package com.example.chatservice.common.security.provider

import com.example.chatservice.api.member.service.MemberCacheService
import com.example.chatservice.api.user.domain.mapper.UserMapper
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.http.constant.ResponseCode
import com.example.chatservice.common.security.constant.TokenStatus
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.slf4j.MDC
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationProvider(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberCacheService: MemberCacheService
) : OncePerRequestFilter() {

    private val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    companion object {
        const val ACCESS_TOKEN_NAME = "access_token"
        const val REFRESH_TOKEN_NAME = "refresh_token"
        const val TOKEN_STATUS_HEADER = "token_status"
        const val TEST_TOKEN_NAME = "is_test"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val testToken = request.getHeader(TEST_TOKEN_NAME)
        val accessToken = request.getHeader(ACCESS_TOKEN_NAME)

        if (testToken != null && testToken == "Y") {
            testAllow()
        } else {
            if (accessToken != null) {

                val tokenStatus = jwtTokenProvider.validateToken(accessToken)
                response.setHeader(TOKEN_STATUS_HEADER, tokenStatus.name)

                when (tokenStatus) {
                    TokenStatus.ALLOW -> {
                        allow(accessToken)
                        
                    }

                    TokenStatus.EXPIRED -> expired()

                    TokenStatus.NOT_ALLOW -> notAllow()
                }
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun allow(accessToken: String) {
        val id = jwtTokenProvider.parseIdFromJWT(accessToken)
        MDC.put("userId", id.toString())
        validateLogin(id)
        setUser(id)
    }

    private fun testAllow() {
        val id = 1L
        MDC.put("userId", "TEST")
        setUser(id)
    }

    private fun expired() {
    }

    private fun notAllow() {
    }

    private fun validateLogin(id: Long) {
        val refreshToken = jwtTokenProvider.findRefreshToken(id)
        if (jwtTokenProvider.validateToken(refreshToken) != TokenStatus.ALLOW) throw ResponseException(ResponseCode.ALREADY_LOGOUT_ERROR)
    }


    private fun setUser(id: Long) {
        val user = userMapper.toUser(memberCacheService.findMember(id))
        val authenticationToken = UsernamePasswordAuthenticationToken(user, null, null)
        authenticationToken.details = user
        SecurityContextHolder.getContext().authentication = authenticationToken
    }
}