package com.example.heroes_fight.data.domain.repository.tcp_ip.receiver

import com.example.heroes_fight.data.domain.model.common.ResultToSendBySocketModel
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import kotlinx.coroutines.flow.MutableSharedFlow

interface ITcpReceiver {

    suspend fun awaitForEnemyActions(
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>,
        enemyFighters: MutableList<FighterModel>,
        ownFighters: MutableList<FighterModel>,
        attackedFighter: FighterModel
    )

    suspend fun receiveMovement(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    )

    suspend fun receivePass(
        action: String,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    )

    suspend fun receiveDefense(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    )

    suspend fun receiveSupport(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    )

    suspend fun receiveSabotage(
        action: String,
        ownFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    )

    suspend fun receiveAttack(
        action: String,
        ownFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>,
        attackedFighter: FighterModel
    )

    suspend fun awaitForData(
        heroes: MutableList<FighterModel>,
        villains: MutableList<FighterModel>,
        allFighters: MutableList<FighterModel>,
        rocks: MutableList<RockModel>
    )
}