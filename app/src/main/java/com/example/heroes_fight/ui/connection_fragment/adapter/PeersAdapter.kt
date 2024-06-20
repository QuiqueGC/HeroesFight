package com.example.heroes_fight.ui.connection_fragment.adapter

import android.annotation.SuppressLint
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.heroes_fight.R
import com.example.heroes_fight.databinding.ItemRecyclerDeviceBinding

class PeersAdapter(
    private val peers: MutableList<WifiP2pDevice>,
    private val listener: ItemDeviceListener
) : RecyclerView.Adapter<PeersAdapter.PeerViewHolder>() {


    interface ItemDeviceListener {
        fun onDeviceClick(device: WifiP2pDevice)
    }

    inner class PeerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecyclerDeviceBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_device, parent, false)
        return PeerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeerViewHolder, position: Int) {
        val device = peers[position]
        holder.binding.tvDeviceName.text = device.deviceName
        holder.itemView.setOnClickListener { listener.onDeviceClick(device) }
    }

    override fun getItemCount(): Int = peers.size

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(peerList: WifiP2pDeviceList) {
        peers.clear()
        peers.addAll(peerList.deviceList)
        notifyDataSetChanged()
    }
}