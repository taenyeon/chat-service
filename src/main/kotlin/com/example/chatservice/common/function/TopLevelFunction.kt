package com.example.chatservice.common.function

import org.mapstruct.factory.Mappers
import com.example.chatservice.api.user.domain.dto.User
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import com.example.chatservice.api.user.domain.mapper.UserMapper
import com.example.chatservice.common.util.EncryptUtil

// LOGGING
inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

// USER
var userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)
fun user() = userMapper.toMember(SecurityContextHolder.getContext().authentication.details as User)

fun String.encrypt() = EncryptUtil().encrypt(this)

fun String.decrypt() = EncryptUtil().decrypt(this)