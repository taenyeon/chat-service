package com.example.chatservice.common.interfaces

interface EntityMapper<E, D> {

    fun toEntity(dto: D): E
    fun toDto(entity: E): D
}