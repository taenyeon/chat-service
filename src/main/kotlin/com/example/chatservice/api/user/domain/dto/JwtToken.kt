package com.example.chatservice.api.user.domain.dto

class JwtToken{
    lateinit var accessToken: String
    lateinit var refreshToken: String

    constructor()
    constructor(refreshToken: String) {
        this.refreshToken = refreshToken
    }
    constructor(accessToken:String, refreshToken:String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
}