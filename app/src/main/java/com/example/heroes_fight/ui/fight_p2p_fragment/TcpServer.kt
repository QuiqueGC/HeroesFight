package com.example.heroes_fight.ui.fight_p2p_fragment

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.ActionResultModel
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
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>,
        villains: MutableList<FighterModel>,
        heroes: MutableList<FighterModel>
    ) {
        var finnishTurn = false
        withContext(Dispatchers.IO) {
            try {
                do {
                    Log.i("skts", "Empieza el bucle")
                    val action = ois.readObject() as String

                    Log.i("skts", "Valor de ACTION -> $action")
                    when (action) {
                        "move" -> emitMovement(action, villains, actionToEmit)
                        "pass" -> {
                            emitPass(action, actionToEmit)
                            finnishTurn = true

                        }

                        "defense" -> emitDefense(action, villains, actionToEmit)
                        "support" -> emitSupport(action, villains, actionToEmit)
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

    private suspend fun emitSupport(
        action: String,
        villains: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val combatBonus = ois.read()
                val allyToSupport = ois.readObject() as FighterModel
                val resultOfSupport = ois.readObject() as ActionResultModel
                for (villain in villains) {
                    if (villain.id == allyToSupport.id) {
                        Log.i("skts", "Coinciden los ID")
                        villain.combatBonus = combatBonus
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

    private suspend fun emitDefense(
        action: String,
        villains: MutableList<FighterModel>,
        actionToEmit: MutableSharedFlow<ResultToSendBySocketModel>
    ) {
        withContext(Dispatchers.IO) {
            try {
                val defenseBonus = ois.read()
                val actualFighter = ois.readObject() as FighterModel
                val resultOfDefense = ois.readObject() as ActionResultModel
                for (villain in villains) {
                    if (villain.id == actualFighter.id) {
                        Log.i("skts", "Coinciden los ID")
                        villain.defenseBonus = defenseBonus
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

    private suspend fun emitPass(
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

    private suspend fun emitMovement(
        action: String,
        villains: MutableList<FighterModel>,
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
                for (villain in villains) {
                    if (villain.id == actualFighter.id) {
                        Log.i("skts", "Coinciden los ID")
                        villain.position = actualPosition
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

    fun sendSupport(allyToSupport: FighterModel, resultOfSupport: ActionResultModel) {
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

    fun sendDefense(actualFighter: FighterModel, resultOfDefense: ActionResultModel) {
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


    fun sendMove(actualFighter: FighterModel) {
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


    suspend fun sendFinnishTurn() {
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


}
