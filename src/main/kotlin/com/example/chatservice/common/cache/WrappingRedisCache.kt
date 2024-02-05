package com.example.chatservice.common.cache

import com.example.chatservice.common.function.logger
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.cache.RedisCache
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheWriter


class WrappingRedisCache(
    name: String,
    cacheWriter: RedisCacheWriter,
    cacheConfiguration: RedisCacheConfiguration,
    private val objectMapper: ObjectMapper,
    private val targetReturnType: Class<*>
) : RedisCache(name, cacheWriter, cacheConfiguration) {

    override fun deserializeCacheValue(value: ByteArray): Any? {
        try {
            return objectMapper.readValue(value, targetReturnType)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}