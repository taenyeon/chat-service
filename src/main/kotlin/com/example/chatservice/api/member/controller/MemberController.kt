package com.example.chatservice.api.member.controller

import com.example.chatservice.api.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.chatservice.common.function.logger
import com.example.chatservice.common.http.constant.ResponseCode
import com.example.chatservice.common.http.domain.Response

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {
    val log = logger()

    @GetMapping("/{username}")
    fun findUser(@PathVariable username: String): ResponseEntity<Response> {
        val member = memberService.findMemberDto(username)
        return ResponseCode.SUCCESS.toResponse(member)
    }

}