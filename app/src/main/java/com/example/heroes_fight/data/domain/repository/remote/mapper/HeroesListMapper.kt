package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroesListResponse

class HeroesListMapper : ResponseMapper<HeroesListResponse, MutableList<HeroEntity>> {
    override fun fromResponse(response: HeroesListResponse): MutableList<HeroEntity> {
        val heroesToReturn = mutableListOf<HeroEntity>()
        response.results.forEach { heroesToReturn.add(HeroMapper().fromResponse(it)) }
        return heroesToReturn
    }
}