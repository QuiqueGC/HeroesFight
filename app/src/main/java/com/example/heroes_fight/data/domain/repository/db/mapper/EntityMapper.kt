package com.example.heroes_fight.data.domain.repository.db.mapper

interface EntityMapper<E, M> {
    fun fromEntity(entity: E): M
}