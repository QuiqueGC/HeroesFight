package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroResponse
import kotlin.random.Random

class HeroMapper : ResponseMapper<HeroResponse, HeroEntity> {
    override fun fromResponse(response: HeroResponse): HeroEntity {

        return HeroEntity(
            response.id?.toInt() ?: 0,
            response.name ?: "Not name available",
            StatsMapper().fromResponse(response.powerstats!!),
            BiographyMapper().fromResponse(response.biography!!),
            AppearanceMapper().fromResponse(response.appearance!!),
            WorkMapper().fromResponse(response.work!!),
            ImgMapper().fromResponse(response.image!!),
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
}