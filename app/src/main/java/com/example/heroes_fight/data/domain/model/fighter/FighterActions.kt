package com.example.heroes_fight.data.domain.model.fighter

import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.RockModel

interface FighterActions {

    fun move(destinationPosition: Position): Boolean

    fun attack(enemy: FighterModel): String

    fun getDefenseRoll(): Int

    fun getIntelligenceRoll(): Int

    fun getDodgeRoll(): Int

    fun sabotage(enemy: FighterModel): String

    fun defense(): String
    fun support(ally: FighterModel): String

    fun shot(
        enemy: FighterModel,
        rocks: List<RockModel>,
        allFightersList: List<FighterModel>
    ): String
}