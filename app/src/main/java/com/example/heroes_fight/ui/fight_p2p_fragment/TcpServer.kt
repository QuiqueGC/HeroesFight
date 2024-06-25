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
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class TcpServer(private val port: Int) {

    private lateinit var serverSocket: ServerSocket
    private lateinit var clientSocket: Socket
    private lateinit var outputStream: OutputStream
    private lateinit var oos: ObjectOutputStream
    private lateinit var inputStream: InputStream
    private lateinit var ois: ObjectInputStream
    suspend fun startServer() {
        //thread {
        withContext(Dispatchers.IO) {
            try {
                serverSocket = ServerSocket(port)
                Log.i("skts", "Server started on port $port")

                //while (true) {
                clientSocket = serverSocket.accept()
                Log.i("skts", "Client connected: ${clientSocket.inetAddress.hostAddress}")

                val input = clientSocket.getInputStream().bufferedReader()
                val output = clientSocket.getOutputStream().bufferedWriter()
                outputStream = clientSocket.getOutputStream()
                inputStream = clientSocket.getInputStream()
                oos = ObjectOutputStream(outputStream)
                ois = ObjectInputStream(inputStream)

                output.write("Hello from Server\n")
                output.flush()

                val clientMessage = input.readLine()
                Log.i("skts", "Received from client: $clientMessage")

                //clientSocket.close()
                //}
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //}
    }

    fun sendFightersList(
        heroes: MutableList<FighterModel>,
        villains: MutableList<FighterModel>,
        allFighters: MutableList<FighterModel>
    ) {
        thread {
            Log.i("skts", "Enviando listas")
            Log.i("skts", "Nº héroes ${heroes.size}")
            Log.i("skts", "Nº villanos ${villains.size}")
            Log.i("skts", "Nº total ${allFighters.size}")

            try {
                //val outputStream = clientSocket.getOutputStream()
                //val oos = ObjectOutputStream(outputStream)
                oos.writeObject(heroes)
                oos.writeObject(villains)
                oos.writeObject(allFighters)
                oos.flush()
                //oos.close()
                Log.i("skts", "Listas enviadas exitosamente")
            } catch (e: Exception) {
                Log.i("skts", "Saltó el catch de enviado de listas")
                Log.i("skts", e.toString())
            }
        }
    }


    suspend fun getRocksFromClient(rocks: MutableList<RockModel>) {
        withContext(Dispatchers.IO) {
            try {
                //val inputStream = socket.getInputStream()
                //val ois = ObjectInputStream(inputStream)

                Log.i("skts", "listas recibidas")
                rocks.addAll(ois.readObject() as MutableList<RockModel>)
                Log.i("skts", "Rocas recibidas! CANTIDAD: ${rocks.size}")
                //ois.close()
            } catch (e: Exception) {

                Log.i("skts", "Saltó el catch de recepción de listas")
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
                    val villainsReceived = ois.readObject() as MutableList<FighterModel>
                    val heroesReceived = ois.readObject() as MutableList<FighterModel>
                    val resultFromServer = ois.readObject() as ResultToSendBySocketModel

                    for (i in heroes.indices) {
                        heroes[i].position.y = heroesReceived[i].position.y
                        heroes[i].position.x = heroesReceived[i].position.x
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
                val heroPositions = mutableListOf<Position>()
                heroes.forEach { heroPositions.add(it.position) }
                oos.writeObject(heroPositions)
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
