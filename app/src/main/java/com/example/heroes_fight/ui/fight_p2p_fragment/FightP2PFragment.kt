package com.example.heroes_fight.ui.fight_p2p_fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.heroes_fight.ui.fight_fragment.FightFragment

class FightP2PFragment : FightFragment() {

    //private val viewModel: FightFragmentViewModel by viewModels()
    override val viewModel: FightP2PFragmentViewModel by viewModels()
    private val args: FightP2PFragmentArgs by navArgs()
    private lateinit var server: TcpServer
    private lateinit var client: TcpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.isServer) {
            Log.i("skts", "Ha entrado en la parte de server")
            server = TcpServer(8888)
            server.startServer()
        } else {
            Log.i("skts", "Ha entrado en la parte de cliente")
            client = TcpClient(
                "192.168.1.140",
                8888
            ) // "10.0.2.2" es la dirección IP del host para los emuladores
            client.connectToServer()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}