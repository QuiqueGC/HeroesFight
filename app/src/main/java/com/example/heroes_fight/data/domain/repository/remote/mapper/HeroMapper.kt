package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroResponse
import kotlin.random.Random

class HeroMapper : ResponseMapper<HeroResponse, HeroModel> {
    override fun fromResponse(response: HeroResponse): HeroModel {

        /*var idHero = ""
        if (response.id != null) {
            when (response.id!!.length) {
                1 -> idHero = "#00${response.id}"
                2 -> idHero = "#0${response.id}"
                3 -> idHero = "#${response.id}"
            }
        }*/

        createSerialNum(response.id)

        return HeroModel(
            response.id?.toInt() ?: 0,
            createSerialNum(response.id),
            response.name ?: "",
            response.biography?.alignment ?: "",
            response.image?.url ?: "",
            addedStatValueInNullCase(response.powerstats?.intelligence),
            addedStatValueInNullCase(response.powerstats?.strength),
            addedStatValueInNullCase(response.powerstats?.speed),
            addedStatValueInNullCase(response.powerstats?.durability),
            addedStatValueInNullCase(response.powerstats?.power),
            addedStatValueInNullCase(response.powerstats?.combat)
        )
    }

    private fun createSerialNum(id: String?): String {
        return if (id != null) {
            when (id.length) {
                1 -> "#00${id}"
                2 -> "#0${id}"
                3 -> "#${id}"
                else -> ""
            }
        } else {
            ""
        }
    }

    private fun addedStatValueInNullCase(statToChange: String?): String {
        return if (statToChange == "null" || statToChange == null) {
            Random.nextInt(5, 51).toString()
        } else {
            statToChange
        }
    }
}