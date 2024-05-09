package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroResponse

class HeroMapper : ResponseMapper<HeroResponse, HeroModel> {
    override fun fromResponse(response: HeroResponse): HeroModel {

        var idHero = ""
        if (response.id != null) {
            when (response.id!!.length) {
                1 -> idHero = "#00${response.id}"
                2 -> idHero = "#0${response.id}"
                3 -> idHero = "#${response.id}"
            }
        }



        return HeroModel(
            idHero,
            response.name ?: "",
            response.biography?.alignment ?: "",
            response.image?.url ?: "",
            response.powerstats?.intelligence?.toInt() ?: 0,
            response.powerstats?.strength?.toInt() ?: 0,
            response.powerstats?.speed?.toInt() ?: 0,
            response.powerstats?.durability?.toInt() ?: 0,
            response.powerstats?.power?.toInt() ?: 0,
            response.powerstats?.combat?.toInt() ?: 0,
        )
    }
}