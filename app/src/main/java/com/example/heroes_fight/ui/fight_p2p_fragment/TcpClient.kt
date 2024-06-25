package com.example.heroes_fight.ui.fight_p2p_fragment

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.ResultToSendBySocketModel
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.Socket
import kotlin.concurrent.thread

class TcpClient(private val serverIp: String, private val serverPort: Int) {

    private lateinit var socket: Socket
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    private lateinit var ois: ObjectInputStream
    private lateinit var oos: ObjectOutputStream

    suspend fun connectToServer() {
        Log.i("skts", "intentará conectarse al server")
        withContext(Dispatchers.IO) {
            try {
                Log.i("skts", "ha entrado en el try")
                socket = Socket(serverIp, serverPort)
                Log.i("skts", "Connected to server at $serverIp:$serverPort")

                val input = socket.getInputStream().bufferedReader()
                val output = socket.getOutputStream().bufferedWriter()
                inputStream = socket.getInputStream()
                outputStream = socket.getOutputStream()
                ois = ObjectInputStream(inputStream)
                oos = ObjectOutputStream(outputStream)
                output.write("Hello from Client\n")
                output.flush()

                val serverMessage = input.readLine()
                Log.i("skts", "Received from server: $serverMessage")

                //socket.close()
            } catch (e: Exception) {
                Log.i("skts", "Ha entrado en el cath?")
                Log.i("skts", e.toString())
                e.printStackTrace()
            }
        }
    }

    suspend fun getFightersList(
        heroes: MutableList<FighterModel>,
        villains: MutableList<FighterModel>,
        allFighters: MutableList<FighterModel>
    ) {
        withContext(Dispatchers.IO) {
            Log.i("skts", "recibiendo listas")
            try {
                //val inputStream = socket.getInputStream()
                //val ois = ObjectInputStream(inputStream)

                Log.i("skts", "listas recibidas")
                heroes.addAll(ois.readObject() as MutableList<FighterModel>)
                villains.addAll(ois.readObject() as MutableList<FighterModel>)
                allFighters.addAll(ois.readObject() as MutableList<FighterModel>)
                Log.i("skts", "Nº héroes ${heroes.size}")
                Log.i("skts", "Nº villanos ${villains.size}")
                Log.i("skts", "Nº total ${allFighters.size}")

                //ois.close()
            } catch (e: Exception) {

                Log.i("skts", "Saltó el catch de recepción de listas")
                Log.i("skts", e.toString())
            }
        }
    }


    suspend fun sendRocks(rocks: MutableList<RockModel>) {
        withContext(Dispatchers.IO) {
            try {
                //val outputStream = clientSocket.getOutputStream()
                //val oos = ObjectOutputStream(outputStream)
                oos.writeObject(rocks)
                oos.flush()
                //oos.close()
                Log.i("skts", "Rocas enviadas! CANTIDAD: ${rocks.size}")
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de enviado rocas")
                Log.i("skts", e.toString())
            }
        }
    }

    suspend fun awaitForEnemyActions(
        actionFromOtherDevice: MutableSharedFlow<ResultToSendBySocketModel>,
        villains: MutableList<FighterModel>,
        heroes: MutableList<FighterModel>
    ) {
        withContext(Dispatchers.IO) {
            try {
                do {

                    val heroPositions = ois.readObject() as MutableList<Position>
                    val villainsReceived = ois.readObject() as MutableList<FighterModel>
                    val heroesReceived = ois.readObject() as MutableList<FighterModel>
                    val resultFromServer = ois.readObject() as ResultToSendBySocketModel

                    for (position in heroPositions) {
                        Log.i("skts", "Posición -> y: ${position.y}, x ${position.x}")
                    }

                    for (i in heroes.indices) {
                        Log.i("skts", "BONUS DE DEFENSA = ${heroes[i].defenseBonus}")

                        heroes[i].position.y = heroPositions[i].y
                        heroes[i].position.x = heroPositions[i].x
                        heroes[i].combatBonus = heroesReceived[i].combatBonus
                        heroes[i].defenseBonus = heroesReceived[i].defenseBonus
                        heroes[i].actionPerformed = heroesReceived[i].actionPerformed
                        heroes[i].movementPerformed = heroesReceived[i].movementPerformed
                        heroes[i].isSabotaged = heroesReceived[i].isSabotaged
                        heroes[i].score = heroesReceived[i].score
                        heroes[i].durability = heroesReceived[i].durability

                        Log.i(
                            "skts",
                            "Posición del héroe recibido $i ->>> " + heroesReceived[i].position.y + "," + heroesReceived[i].position.x
                        )
                        Log.i(
                            "skts",
                            "Posición del héroe $i ->>> " + heroes[i].position.y + "," + heroes[i].position.x
                        )

                        villains[i].position.y = villainsReceived[i].position.y
                        villains[i].position.x = villainsReceived[i].position.x
                        villains[i].combatBonus = villainsReceived[i].combatBonus
                        villains[i].defenseBonus = villainsReceived[i].defenseBonus
                        villains[i].actionPerformed = villainsReceived[i].actionPerformed
                        villains[i].movementPerformed = villainsReceived[i].movementPerformed
                        villains[i].isSabotaged = villainsReceived[i].isSabotaged
                        villains[i].score = villainsReceived[i].score
                        villains[i].durability = villainsReceived[i].durability

                        Log.i(
                            "skts",
                            "Posición del villano recibido $i ->>> " + villainsReceived[i].position.y + "," + villainsReceived[i].position.x
                        )
                        Log.i(
                            "skts",
                            "Posición del villano $i ->>> " + villains[i].position.y + "," + villains[i].position.x
                        )
                    }

                    actionFromOtherDevice.emit(
                        ResultToSendBySocketModel(
                            resultFromServer.txtToTvInfo,
                            resultFromServer.txtToTvActionResult,
                            resultFromServer.action,
                            resultFromServer.finnishTurn
                        )
                    )

                } while (!resultFromServer.finnishTurn)
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de recepción de acción")
                Log.i("skts", e.toString())
            }
        }
    }

    fun sendAction(
        resultToShow: ResultToSendBySocketModel,
        villains: MutableList<FighterModel>,
        heroes: MutableList<FighterModel>
    ) {
        thread {
            heroes.forEach {
                Log.i(
                    "skts",
                    "Posición de héroe enviada->>> " + it.position.y + "," + it.position.x
                )
            }
            villains.forEach {
                Log.i(
                    "skts",
                    "Posición de villano enviada->>> " + it.position.y + "," + it.position.x
                )
            }
            try {
                oos.writeObject(villains)
                oos.writeObject(heroes)
                oos.writeObject(resultToShow)

            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de envío de acción")
                Log.i("skts", e.toString())
            }
        }
    }
}
