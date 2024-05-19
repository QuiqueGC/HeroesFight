package com.example.heroes_fight.data.domain.model.fighter

import com.example.heroes_fight.data.domain.model.common.Position

interface FighterActions {

    fun move(destinationPosition: Position): Boolean

    fun attack(enemy: FighterModel): String

    fun defenceRoll(): Int
}