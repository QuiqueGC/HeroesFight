package com.example.heroes_fight.data.domain.model

import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.hero.HeroModel

interface Fighter {

    fun move(destinationPosition: Position): Boolean

    fun attack(enemy: HeroModel): String

    fun defenceRoll(): Int
}