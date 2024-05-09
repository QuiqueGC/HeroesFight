package com.example.heroes_fight.data.domain.repository.remote.mapper

interface ResponseMapper<R, M> {
    fun fromResponse(response: R): M
}