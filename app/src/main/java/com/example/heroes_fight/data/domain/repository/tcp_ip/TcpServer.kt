package com.example.heroes_fight.data.domain.repository.tcp_ip

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.ActionResultModel
import com.example.heroes_fight.data.domain.model.common.ResultToSendBySocketModel
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.tcp_ip.receiver.ITcpReceiver
import com.example.heroes_fight.data.domain.repository.tcp_ip.receiver.TcpReceiver
import com.example.heroes_fight.data.domain.repository.tcp_ip.sender.ITcpSender
import com.example.heroes_fight.data.domain.repository.tcp_ip.sender.TcpSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.net.ServerSocket

class TcpServer(private val port: Int) : ITcpSender, ITcpReceiver {

    private lateinit var tcpSender: TcpSender
    private lateinit var tcpReceiver: TcpReceiver

    suspend fun startServer() {

        withContext(Dispatchers.IO) {
            try {
                val serverSocket = ServerSocket(port)
                Log.i("skts", "Server started on port $port")

                val clientSocket = serverSocket.accept()
                Log.i("skts", "Client connected: ${clientSocket.inetAddress.hostAddress}")
                tcpSender = TcpSender(clientSocket)
                tcpReceiver = TcpReceiver(clientSocket)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun awaitForEnemyActions(
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>,
        enemyFighters: MutableList<FighterModel>,
        ownFighters: MutableList<FighterModel>,
        attackedFighter: FighterModel
    ) {
        tcpReceiver.awaitForEnemyActions(actionToEmit, enemyFighters, ownFighters, attackedFighter)
    }


    override suspend fun sendFinnishTurn() {
        tcpSender.sendFinnishTurn()
    }

    override fun sendMove(actualFighter: FighterModel) {
        tcpSender.sendMove(actualFighter)
    }

    override fun sendDefense(actualFighter: FighterModel, resultOfDefense: ActionResultModel) {
        tcpSender.sendDefense(actualFighter, resultOfDefense)
    }

    override fun sendSupport(allyToSupport: FighterModel, resultOfSupport: ActionResultModel) {
        tcpSender.sendSupport(allyToSupport, resultOfSupport)
    }

    override fun sendSabotage(enemyToSabotage: FighterModel, resultOfSabotage: ActionResultModel) {
        tcpSender.sendSabotage(enemyToSabotage, resultOfSabotage)
    }

    override fun sendAttack(enemyToAttack: FighterModel, resultOfAttack: ActionResultModel) {
        tcpSender.sendAttack(enemyToAttack, resultOfAttack)
    }

    override fun sendDataToFight(
        heroes: MutableList<FighterModel>,
        villains: MutableList<FighterModel>,
        allFighters: MutableList<FighterModel>,
        rocks: MutableList<RockModel>
    ) {
        tcpSender.sendDataToFight(heroes, villains, allFighters, rocks)
    }

    override suspend fun receiveMovement(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        tcpReceiver.receiveMovement(action, enemyFighters, actionToEmit)
    }

    override suspend fun receivePass(
        action: String,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        tcpReceiver.receivePass(action, actionToEmit)
    }

    override suspend fun receiveDefense(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        tcpReceiver.receiveDefense(action, enemyFighters, actionToEmit)
    }

    override suspend fun receiveSupport(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        tcpReceiver.receiveSupport(action, enemyFighters, actionToEmit)
    }

    override suspend fun receiveSabotage(
        action: String,
        ownFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        tcpReceiver.receiveSabotage(action, ownFighters, actionToEmit)
    }

    override suspend fun receiveAttack(
        action: String,
        ownFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>,
        attackedFighter: FighterModel
    ) {
        tcpReceiver.receiveAttack(action, ownFighters, actionToEmit, attackedFighter)
    }

    override suspend fun awaitForData(
        heroes: MutableList<FighterModel>,
        villains: MutableList<FighterModel>,
        allFighters: MutableList<FighterModel>,
        rocks: MutableList<RockModel>
    ) {
    }
}
