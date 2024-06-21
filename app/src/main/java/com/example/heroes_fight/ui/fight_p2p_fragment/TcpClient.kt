package com.example.heroes_fight.ui.fight_p2p_fragment

import android.util.Log
import java.net.Socket
import kotlin.concurrent.thread

class TcpClient(private val serverIp: String, private val serverPort: Int) {

    fun connectToServer() {
        Log.i("skts", "intentará conectarse al server")
        thread {
            try {
                Log.i("skts", "ha entrado en el try")
                val socket = Socket(serverIp, serverPort)
                Log.i("skts", "Connected to server at $serverIp:$serverPort")

                val input = socket.getInputStream().bufferedReader()
                val output = socket.getOutputStream().bufferedWriter()

                output.write("Hello from Client\n")
                output.flush()

                val serverMessage = input.readLine()
                Log.i("skts", "Received from server: $serverMessage")

                socket.close()
            } catch (e: Exception) {
                Log.i("skts", "Ha entrado en el cath?")
                Log.i("skts", e.toString())
                e.printStackTrace()
            }
        }
    }
}
