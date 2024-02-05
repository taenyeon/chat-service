package com.example.chatservice.common.message.principal

import java.security.Principal

class ChatUser(
    private var name: String
) : Principal {
    override fun getName(): String {
        return this.name
    }
}