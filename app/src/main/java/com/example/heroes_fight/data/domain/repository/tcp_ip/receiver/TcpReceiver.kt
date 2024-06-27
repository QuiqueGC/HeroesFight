package com.example.heroes_fight.data.domain.repository.tcp_ip.receiver

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.ActionResultModel
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.ResultToSendBySocketModel
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.ObjectInputStream
import java.net.Socket

class TcpReceiver(socket: Socket) : ITcpReceiver {

    private val inputStream = socket.getInputStream()
    private val ois = ObjectInputStream(inputStream)
    override suspend fun awaitForEnemyActions(
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>,
        enemyFighters: MutableList<FighterModel>,
        ownFighters: MutableList<FighterModel>,
        attackedFighter: FighterModel
    ) {
        var finnishTurn = false
        withContext(Dispatchers.IO) {
            try {
                do {
                    Log.i("skts", "Empieza el bucle")
                    val action = ois.readObject() as String

                    Log.i("skts", "Valor de ACTION -> $action")
                    when (action) {
                        "move" -> receiveMovement(action, enemyFighters, actionToEmit)
                        "pass" -> {
                            receivePass(action, actionToEmit)
                            finnishTurn = true

                        }

                        "defense" -> receiveDefense(action, enemyFighters, actionToEmit)
                        "support" -> receiveSupport(action, enemyFighters, actionToEmit)
                        "sabotage" -> receiveSabotage(action, ownFighters, actionToEmit)
                        "attack" -> receiveAttack(
                            action,
                            ownFighters,
                            actionToEmit,
                            attackedFighter
                        )
                    }
                    Log.i(
                        "skts",
                        "Llega al final del bucle"
                    )
                } while (!finnishTurn)
                Log.i(
                    "skts",
                    "Salió del bucle del threat que espera acciones"
                )
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de recepción de acción")
                Log.i("skts", e.toString())
            }
        }
    }

    override suspend fun receiveMovement(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val actualPosition = ois.readObject() as Position
                Log.i(
                    "skts",
                    "Posición recibida ${actualPosition.y} ${actualPosition.x}"
                )
                val actualFighter = ois.readObject() as FighterModel
                Log.i(
                    "skts",
                    "Posición recibida dentro del hero ${actualFighter.position.y} ${actualFighter.position.x}"
                )
                Log.i("skts", "ID del actualFighter ${actualFighter.id}")
                for (fighter in enemyFighters) {
                    if (fighter.id == actualFighter.id) {
                        Log.i("skts", "Coinciden los ID")
                        fighter.position = actualPosition
                    }
                }
                actionToEmit.emit(
                    ResultToSendBySocketModel(
                        action = action
                    )
                )
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de recepción de movimiento")
                Log.i("skts", e.toString())
            }
        }
    }

    override suspend fun receivePass(
        action: String,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        withContext(Dispatchers.IO) {

            Log.i("skts", "Recibió pass y ahora lo emite")
            actionToEmit.emit(
                ResultToSendBySocketModel(
                    action = action
                )
            )
        }
    }

    override suspend fun receiveDefense(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val defenseBonus = ois.read()
                val actualFighter = ois.readObject() as FighterModel
                val resultOfDefense = ois.readObject() as ActionResultModel
                for (fighter in enemyFighters) {
                    if (fighter.id == actualFighter.id) {
                        Log.i("skts", "Coinciden los ID")
                        fighter.defenseBonus = defenseBonus
                    }
                }
                actionToEmit.emit(
                    ResultToSendBySocketModel(
                        resultOfDefense.txtToTvInfo,
                        resultOfDefense.txtToTvActionResult,
                        action
                    )
                )
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de recepción de defensa")
                Log.i("skts", e.toString())
            }
        }
    }

    override suspend fun receiveSupport(
        action: String,
        enemyFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val combatBonus = ois.read()
                val allyToSupport = ois.readObject() as FighterModel
                val resultOfSupport = ois.readObject() as ActionResultModel
                for (fighter in enemyFighters) {
                    if (fighter.id == allyToSupport.id) {
                        Log.i("skts", "Coinciden los ID")
                        fighter.combatBonus = combatBonus
                    }
                }
                actionToEmit.emit(
                    ResultToSendBySocketModel(
                        resultOfSupport.txtToTvInfo,
                        resultOfSupport.txtToTvActionResult,
                        action
                    )
                )
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de recepción de defensa")
                Log.i("skts", e.toString())
            }
        }
    }

    override suspend fun receiveSabotage(
        action: String,
        ownFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val isSabotaged = ois.readObject() as Boolean
                val enemyToSabotage = ois.readObject() as FighterModel
                val resultOfSabotage = ois.readObject() as ActionResultModel
                for (fighter in ownFighters) {
                    if (fighter.id == enemyToSabotage.id) {
                        Log.i("skts", "Coinciden los ID")
                        fighter.isSabotaged = isSabotaged
                    }
                }
                actionToEmit.emit(
                    ResultToSendBySocketModel(
                        resultOfSabotage.txtToTvInfo,
                        resultOfSabotage.txtToTvActionResult,
                        action
                    )
                )
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de recepción de defensa")
                Log.i("skts", e.toString())
            }
        }
    }

    override suspend fun receiveAttack(
        action: String,
        ownFighters: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>,
        attackedFighter: FighterModel
    ) {
        withContext(Dispatchers.IO) {
            try {
                val durability = ois.readObject() as Int
                val enemyToAttack = ois.readObject() as FighterModel
                val resultOfAttack = ois.readObject() as ActionResultModel
                for (fighter in ownFighters) {
                    if (fighter.id == enemyToAttack.id) {
                        Log.i("skts", "Coinciden los ID")
                        Log.i("skts", "Valor de durability recibido -> $durability")
                        fighter.durability = durability
                        attackedFighter.id = enemyToAttack.id
                        attackedFighter.durability = durability
                        if (fighter.durability <= 0) {
                            fighter.score.survived = false
                            attackedFighter.score.survived = false
                            Log.i("skts", "Se ha establecido a false el survived")
                        }
                    }
                }
                actionToEmit.emit(
                    ResultToSendBySocketModel(
                        resultOfAttack.txtToTvInfo,
                        resultOfAttack.txtToTvActionResult,
                        action
                    )
                )
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de recepción de ataque")
                Log.i("skts", e.toString())
            }
        }
    }

    override suspend fun awaitForData(
        heroes: MutableList<FighterModel>,
        villains: MutableList<FighterModel>,
        allFighters: MutableList<FighterModel>,
        rocks: MutableList<RockModel>
    ) {
        withContext(Dispatchers.IO) {
            Log.i("skts", "recibiendo listas")
            try {

                Log.i("skts", "listas recibidas")
                heroes.addAll(ois.readObject() as MutableList<FighterModel>)
                villains.addAll(ois.readObject() as MutableList<FighterModel>)
                allFighters.addAll(ois.readObject() as MutableList<FighterModel>)
                rocks.addAll(ois.readObject() as MutableList<RockModel>)
                Log.i("skts", "Nº héroes ${heroes.size}")
                Log.i("skts", "Nº villanos ${villains.size}")
                Log.i("skts", "Nº total ${allFighters.size}")

            } catch (e: Exception) {

                Log.i("skts", "Saltó el catch de recepción de listas")
                Log.i("skts", e.toString())
            }
        }
    }
}