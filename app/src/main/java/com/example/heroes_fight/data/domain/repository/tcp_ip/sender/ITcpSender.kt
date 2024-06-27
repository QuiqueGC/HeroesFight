package com.example.heroes_fight.data.domain.repository.tcp_ip.sender

import com.example.heroes_fight.data.domain.model.common.ActionResultModel
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel

interface ITcpSender {
    suspend fun sendFinnishTurn()
    fun sendMove(actualFighter: FighterModel)
    fun sendDefense(actualFighter: FighterModel, resultOfDefense: ActionResultModel)
    fun sendSupport(allyToSupport: FighterModel, resultOfSupport: ActionResultModel)
    fun sendSabotage(enemyToSabotage: FighterModel, resultOfSabotage: ActionResultModel)
    fun sendAttack(enemyToAttack: FighterModel, resultOfAttack: ActionResultModel)
    fun sendDataToFight(
        heroes: MutableList<FighterModel>,
        villains: MutableList<FighterModel>,
        allFighters: MutableList<FighterModel>,
        rocks: MutableList<RockModel>
    )
}