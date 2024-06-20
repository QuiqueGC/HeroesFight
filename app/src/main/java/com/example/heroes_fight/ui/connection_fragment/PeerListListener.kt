package com.example.heroes_fight.ui.connection_fragment

import android.net.wifi.p2p.WifiP2pDeviceList

interface PeerListListener {
    fun onPeersAvailable(peerList: WifiP2pDeviceList)
}