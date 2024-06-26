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
        var finnishTurn = false
        withContext(Dispatchers.IO) {
            try {
                do {
                    val action = ois.readObject() as String
                    Log.i("skts", "Valor de ACTION -> $action")
                    when (action) {
                        "move" -> {
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
                            for (hero in heroes) {
                                if (hero.id == actualFighter.id) {
                                    Log.i("skts", "Coinciden los ID")
                                    hero.position = actualPosition
                                }
                            }
                            actionFromOtherDevice.emit(
                                ResultToSendBySocketModel(
                                    action = action
                                )
                            )
                        }

                        "pass" -> {
                            finnishTurn = true
                            Log.i(
                                "skts",
                                "Recibió pass y ahora lo emite"
                            )
                            actionFromOtherDevice.emit(
                                ResultToSendBySocketModel(
                                    action = action
                                )
                            )
                        }
                    }

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
