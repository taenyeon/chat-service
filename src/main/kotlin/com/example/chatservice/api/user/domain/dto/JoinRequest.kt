package com.example.chatservice.api.user.domain.dto

import jakarta.validation.constraints.NotNull

class JoinRequest{
    @NotNull
    lateinit var username: String
    @NotNull
    lateinit var password: String
    @NotNull
    lateinit var name: String
    @NotNull
    lateinit var phoneNumber: String
}