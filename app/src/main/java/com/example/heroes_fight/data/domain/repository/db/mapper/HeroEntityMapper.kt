package com.example.heroes_fight.data.domain.repository.db.mapper

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity

class HeroEntityMapper : EntityMapper<HeroEntity, HeroModel> {
    override fun fromEntity(entity: HeroEntity): HeroModel {
        return HeroModel(
            entity.id,
            createSerialNum(entity.id),
            entity.name,
            entity.biography.alignment,
            entity.image.url,
            entity.powerstats.intelligence,
            entity.powerstats.strength,
            entity.powerstats.speed,
            entity.powerstats.durability,
            entity.powerstats.power,
            entity.powerstats.combat
        )
    }

    private fun createSerialNum(id: Int): String {
        val idString = id.toString()
        return when (idString.length) {
            1 -> "#00${id}"
            2 -> "#0${id}"
            3 -> "#${id}"
            else -> ""
        }
    }
}