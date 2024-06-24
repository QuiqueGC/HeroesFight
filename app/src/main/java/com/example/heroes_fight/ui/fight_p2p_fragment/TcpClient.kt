package com.example.heroes_fight.ui.fight_p2p_fragment

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.Socket

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


}
