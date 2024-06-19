package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroesListResponse

class HeroesListMapper : ResponseMapper<HeroesListResponse, MutableList<HeroModel>> {
    override fun fromResponse(response: HeroesListResponse): MutableList<HeroModel> {
        val heroesToReturn = mutableListOf<HeroModel>()
        response.results.forEach { heroesToReturn.add(HeroMapper().fromResponse(it)) }
        return heroesToReturn
    }
}