package com.example.chatservice.common.security.provider

import com.example.chatservice.api.user.domain.dto.JwtToken
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.http.constant.ResponseCode
import com.example.chatservice.common.security.constant.TokenStatus
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.Date
import java.util.concurrent.TimeUnit

@Component
class JwtTokenProvider(
    private val redisTemplate: RedisTemplate<String, Any>,
) {


    companion object {
        const val JWT_SECRET =
            "Y2hvcHBhLWRvbnQtYml0ZS1tZS1zcHJpbmctYm9vdC1qd3QtdGVzdC1zZWNyZXQta2V5LWNob3BwYS1kb250LWJpdGUtbWUtc3ByaW5nLWJvb3Qtand0LXRlc3Qtc2VjcmV0LWtleQo"
        const val REFRESH_TOKEN_KEY = "refreshToken::"

        val ACCESS_TOKEN_EXPIRATION_MS = Duration.ofDays(15).toMillis()
        val REFRESH_TOKEN_EXPIRATION_MS = Duration.ofDays(30).toMillis()
    }

    fun generateToken(id: Long): JwtToken {
        val now = Date()
        return JwtToken(
            generateAccessToken(id, now),
            generateRefreshToken(id, now)
        )
    }

    fun generateAccessToken(id: Long, now: Date): String {
        val expiredDate = Date(now.time + ACCESS_TOKEN_EXPIRATION_MS)
        return Jwts.builder()
            .setSubject(id.toString())
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
            .compact()!!
    }

    fun generateRefreshToken(id: Long, now: Date): String {
        val expiredDate = Date(now.time + REFRESH_TOKEN_EXPIRATION_MS)
        val refreshToken = Jwts.builder()
            .setSubject(id.toString())
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
            .compact()

        val key = REFRESH_TOKEN_KEY + id
        redisTemplate.opsForValue()[key] = refreshToken
        redisTemplate.expire(key, REFRESH_TOKEN_EXPIRATION_MS, TimeUnit.MILLISECONDS)
        return refreshToken!!
    }

    fun findRefreshToken(id: Long): String {
        val key = REFRESH_TOKEN_KEY + id
        val refreshToken = redisTemplate.opsForValue()[key] ?: throw ResponseException(ResponseCode.INVALID_TOKEN)
        return refreshToken as String
    }

    fun dropRefreshToken(id: Long) {
        val key = REFRESH_TOKEN_KEY + id
        redisTemplate.delete(key)
    }

    fun parseIdFromJWT(token: String): Long {
        return try {
            Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .body.subject.toLong()
        } catch (e: Exception) {
            throw ResponseException(ResponseCode.INVALID_REQUEST_PARAM, e)
        }
    }

    fun validateToken(token: String): TokenStatus {
        return try {
            Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
            TokenStatus.ALLOW
        } catch (e: ExpiredJwtException) {
            TokenStatus.EXPIRED
        } catch (e: Exception) {
            TokenStatus.NOT_ALLOW
        }
    }


}