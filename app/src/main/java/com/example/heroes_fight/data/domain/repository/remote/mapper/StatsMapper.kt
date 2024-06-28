package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.repository.db.entity.StatsEntity
import com.example.heroes_fight.data.domain.repository.remote.response.hero.StatsResponse
import kotlin.random.Random

class StatsMapper : ResponseMapper<StatsResponse, StatsEntity> {
    override fun fromResponse(response: StatsResponse): StatsEntity {
        return StatsEntity(
            addedStatValueInNullCase(response.intelligence),
            addedStatValueInNullCase(response.strength),
            addedStatValueInNullCase(response.speed),
            addedStatValueInNullCase(response.durability),
            addedStatValueInNullCase(response.power),
            addedStatValueInNullCase(response.combat)
        )
    }

    private fun addedStatValueInNullCase(statToChange: String?): Int {
        return if (statToChange == "null" || statToChange == null) {
            Random.nextInt(5, 51)
        } else {
            statToChange.toInt()
        }
    }
}