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
}
