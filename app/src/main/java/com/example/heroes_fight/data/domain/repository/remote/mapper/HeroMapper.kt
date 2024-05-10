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
            response.powerstats?.intelligence ?: "",
            response.powerstats?.strength ?: "",
            response.powerstats?.speed ?: "",
            response.powerstats?.durability ?: "",
            response.powerstats?.power ?: "",
            response.powerstats?.combat ?: "",
        )
    }
}