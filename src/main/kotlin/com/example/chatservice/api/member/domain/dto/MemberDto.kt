package com.example.chatservice.api.member.domain.dto

class MemberDto(
    var id: Long?,
    var username: String,
    var name: String,
    var phoneNumber:String,
    var createdAt: String,
    var updatedAt: String,
    var imageUrl: String?,
) {

}