package com.example.heroes_fight.data.domain.repository.db.mapper

import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import kotlin.random.Random

class FighterEntityMapper : EntityMapper<HeroEntity, FighterModel> {
    override fun fromEntity(entity: HeroEntity): FighterModel {

        val speed = addedStatValueIfIsLowValue(entity.powerstats.speed)
        val distanceToShot = addedStatValueIfIsLowValue(entity.powerstats.power)

        return FighterModel(
            entity.id,
            createSerialNum(entity.id),
            entity.name,
            entity.biography.alignment,
            entity.image.url,
            addedStatValueIfIsLowValue(entity.powerstats.intelligence),
            addedStatValueIfIsLowValue(entity.powerstats.strength),
            speed,
            addedStatValueIfIsLowValue(entity.powerstats.durability),
            addedStatValueIfIsLowValue(entity.powerstats.power),
            addedStatValueIfIsLowValue(entity.powerstats.combat),
            setCapacityToMax10(speed),
            setCapacityToMax10(distanceToShot)
        )
    }


    private fun createSerialNum(id: Int): String {
        return when (id.toString().length) {
            1 -> "#00${id}"
            2 -> "#0${id}"
            3 -> "#${id}"
            else -> ""
        }
    }
}

private fun addedStatValueIfIsLowValue(statToChange: Int): Int {
    return if (statToChange < 5) {
        Random.nextInt(5, 51)
    } else {
        statToChange
    }
}

private fun setCapacityToMax10(statToDivide: Int): Int {
    return if (statToDivide / 10 < 1) {
        1
    } else {
        statToDivide / 10
    }

}