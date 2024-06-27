package com.example.heroes_fight.data.domain.repository.tcp_ip.sender

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.ActionResultModel
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ObjectOutputStream
import java.net.Socket
import kotlin.concurrent.thread

class TcpSender(socket: Socket) : ITcpSender {

    private val outputStream = socket.getOutputStream()
    private val oos = ObjectOutputStream(outputStream)
    override suspend fun sendFinnishTurn() {
        withContext(Dispatchers.IO) {
            try {
                Log.i("skts", "Envía pass")
                oos.writeObject("pass")

            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de envío de finalizar turno")
                Log.i("skts", e.toString())
            }
        }
    }

    override fun sendMove(actualFighter: FighterModel) {
        thread {
            try {
                oos.writeObject("move")
                Log.i(
                    "skts",
                    "Enviando posición ${actualFighter.position.y} ${actualFighter.position.x}"
                )
                oos.writeObject(Position(actualFighter.position.y, actualFighter.position.x))
                oos.writeObject(actualFighter)
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de envío de movimiento")
                Log.i("skts", e.toString())
            }
        }
    }

    override fun sendDefense(
        actualFighter: FighterModel,
        resultOfDefense: ActionResultModel
    ) {
        thread {
            val defenseBonus = actualFighter.defenseBonus
            try {
                oos.writeObject("defense")
                oos.write(defenseBonus)
                oos.writeObject(actualFighter)
                oos.writeObject(resultOfDefense)
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de enviar defense")
                Log.i("skts", e.toString())
            }
        }
    }

    override fun sendSupport(
        allyToSupport: FighterModel,
        resultOfSupport: ActionResultModel
    ) {
        thread {
            val combatBonus = allyToSupport.combatBonus
            Log.i("skts", "Valor de combatBonus -> $combatBonus")
            try {
                oos.writeObject("support")
                oos.write(combatBonus)
                oos.writeObject(allyToSupport)
                oos.writeObject(resultOfSupport)
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de enviar support")
                Log.i("skts", e.toString())
            }
        }
    }

    override fun sendSabotage(
        enemyToSabotage: FighterModel,
        resultOfSabotage: ActionResultModel
    ) {
        thread {
            val isSabotaged = enemyToSabotage.isSabotaged

            try {
                oos.writeObject("sabotage")
                oos.writeObject(isSabotaged)
                oos.writeObject(enemyToSabotage)
                oos.writeObject(resultOfSabotage)
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de enviar support")
                Log.i("skts", e.toString())
            }
        }
    }

    override fun sendAttack(
        enemyToAttack: FighterModel,
        resultOfAttack: ActionResultModel
    ) {
        thread {
            val durability = enemyToAttack.durability
            Log.i("skts", "Valor de durability -> $durability")
            try {
                oos.writeObject("attack")
                oos.writeObject(durability)
                oos.writeObject(enemyToAttack)
                oos.writeObject(resultOfAttack)
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de enviar support")
                Log.i("skts", e.toString())
            }
        }
    }

    override fun sendDataToFight(
        heroes: MutableList<FighterModel>,
        villains: MutableList<FighterModel>,
        allFighters: MutableList<FighterModel>,
        rocks: MutableList<RockModel>
    ) {
        thread {
            Log.i("skts", "Enviando listas")
            Log.i("skts", "Nº héroes ${heroes.size}")
            Log.i("skts", "Nº villanos ${villains.size}")
            Log.i("skts", "Nº total ${allFighters.size}")

            try {
                oos.writeObject(heroes)
                oos.writeObject(villains)
                oos.writeObject(allFighters)
                oos.writeObject(rocks)
                oos.flush()

                Log.i("skts", "Listas enviadas exitosamente")
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de enviado de listas")
                Log.i("skts", e.toString())
            }
        }
    }
}