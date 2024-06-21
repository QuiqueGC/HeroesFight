package com.example.heroes_fight.ui.fight_p2p_fragment

import android.util.Log
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class TcpServer(private val port: Int) {

    fun startServer() {
        thread {
            try {
                val serverSocket = ServerSocket(port)
                Log.i("skts", "Server started on port $port")

                while (true) {
                    val clientSocket: Socket = serverSocket.accept()
                    Log.i("skts", "Client connected: ${clientSocket.inetAddress.hostAddress}")

                    val input = clientSocket.getInputStream().bufferedReader()
                    val output = clientSocket.getOutputStream().bufferedWriter()

                    output.write("Hello from Server\n")
                    output.flush()

                    val clientMessage = input.readLine()
                    Log.i("skts", "Received from client: $clientMessage")

                    clientSocket.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
