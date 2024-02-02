package com.example.chatservice.api.user.domain.dto

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

class User(
    var id: Long? = 0,

    private var username: String,

    private var password: String,

    var name: String,

    var phoneNumber: String,

    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    var createdAt: LocalDateTime,

    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    var updatedAt: LocalDateTime
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}