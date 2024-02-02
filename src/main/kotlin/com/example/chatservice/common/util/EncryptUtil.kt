package com.example.chatservice.common.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import com.example.chatservice.common.encrypt.annotation.Decrypt
import com.example.chatservice.common.encrypt.annotation.Encrypt
import com.example.chatservice.common.encrypt.annotation.PasswordEncrypt
import com.example.chatservice.common.exception.ResponseException
import com.example.chatservice.common.http.constant.ResponseCode
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


@Component
class EncryptUtil {
    var alg = "AES/CBC/PKCS5Padding"
    private val key = "12345678910111213"
    private val iv: String = key.substring(0, 16)
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    @Encrypt
    fun encrypt(data: String): String {
        try {
            val cipher = Cipher.getInstance(alg)
            val keySpec = SecretKeySpec(iv.toByteArray(), "AES")
            val ivParamSpec = IvParameterSpec(iv.toByteArray())
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec)

            val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
            return Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            throw ResponseException(ResponseCode.ENCRYPT_ERROR)
        }
    }

    @Decrypt
    fun decrypt(data: String): String {
        try {
            val cipher = Cipher.getInstance(alg)
            val keySpec = SecretKeySpec(iv.toByteArray(), "AES")
            val ivParamSpec = IvParameterSpec(iv.toByteArray())
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec)
            val decodedBytes: ByteArray = Base64.getDecoder().decode(data)
            val decrypted = cipher.doFinal(decodedBytes)
            return String(decrypted, charset(Charsets.UTF_8.name()))
        } catch (e: Exception) {
            throw ResponseException(ResponseCode.DECRYPT_ERROR,e)
        }
    }

    @PasswordEncrypt
    fun passwordEncrypt(password: String): String? {
        return passwordEncoder.encode(password)
    }
}