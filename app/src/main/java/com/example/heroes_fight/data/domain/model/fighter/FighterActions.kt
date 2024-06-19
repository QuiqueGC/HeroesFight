package com.example.heroes_fight.data.domain.model.fighter

import com.example.heroes_fight.data.domain.model.common.ActionResultModel
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.RockModel

interface FighterActions {

    fun move(destinationPosition: Position): Boolean

    fun attack(enemy: FighterModel): ActionResultModel

    fun getDefenseRoll(): Int

    fun getIntelligenceRoll(): Int

    fun getDodgeRoll(): Int

    fun sabotage(enemy: FighterModel): ActionResultModel

    fun defense(): ActionResultModel
    fun support(ally: FighterModel): ActionResultModel

    fun shot(
        enemy: FighterModel,
        rocks: List<RockModel>,
        allFightersList: List<FighterModel>
    ): ActionResultModel
}