package com.example.chatservice.common.jpa.entity

import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseTimeEntity {

    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    lateinit var createdAt: LocalDateTime
    @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    lateinit var updatedAt: LocalDateTime

    @PrePersist
    fun prePersist() {
        val now = LocalDateTime.now()
        this.createdAt = now
        this.updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}