package com.example.chatservice.common.paging.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort


class Page {
    var currentPage : Int = 0
    var searchSize : Int = 10

    constructor()
    constructor(currentPage : Int, searchSize: Int) {
        this.currentPage = currentPage
        this.searchSize = searchSize
    }

    fun request(): PageRequest {
        return PageRequest.of(currentPage, searchSize)
    }
}