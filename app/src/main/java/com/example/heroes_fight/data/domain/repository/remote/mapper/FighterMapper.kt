package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroResponse
import kotlin.random.Random

class FighterMapper : ResponseMapper<HeroResponse, FighterModel> {
    override fun fromResponse(response: HeroResponse): FighterModel {

        val speed = addedStatValueInNullCase(response.powerstats?.speed)
        val distance = addedStatValueInNullCase(response.powerstats?.speed)

        return FighterModel(
            response.id?.toInt() ?: 0,
            createSerialNum(response.id),
            response.name ?: "",
            response.biography?.alignment ?: "",
            response.image?.url ?: "",
            addedStatValueInNullCase(response.powerstats?.intelligence),
            addedStatValueInNullCase(response.powerstats?.strength),
            speed,
            addedStatValueInNullCase(response.powerstats?.durability),
            addedStatValueInNullCase(response.powerstats?.power),
            addedStatValueInNullCase(response.powerstats?.combat),
            setCapacityToMax10(speed),
            setCapacityToMax10(distance)
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

    private fun addedStatValueInNullCase(statToChange: String?): Int {
        return if (statToChange == "null" || statToChange == null) {
            Random.nextInt(5, 51)
        } else {
            statToChange.toInt()
        }
    }

    private fun setCapacityToMax10(speed: Int): Int {
        return if (speed / 10 < 1) {
            1
        } else {
            speed / 10
        }
    }
}