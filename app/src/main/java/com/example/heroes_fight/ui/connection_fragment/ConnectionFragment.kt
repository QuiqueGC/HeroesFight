package com.example.heroes_fight.ui.connection_fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heroes_fight.databinding.FragmentConnectionBinding
import com.example.heroes_fight.ui.connection_fragment.adapter.PeersAdapter


class ConnectionFragment : Fragment(), WifiP2pManager.PeerListListener,
    PeersAdapter.ItemDeviceListener {

    private lateinit var binding: FragmentConnectionBinding
    private lateinit var adapter: PeersAdapter


    private val intentFilter = IntentFilter()
    private lateinit var manager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var receiver: BroadcastReceiver
    private var peers: MutableList<WifiP2pDevice> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConnectionBinding.inflate(inflater, container, false)

        setupIntentFilter()
        manager = requireActivity().getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(requireContext(), Looper.getMainLooper(), null)
        receiver = WifiDirectBroadcastReceiver(manager, channel, this)
        requireActivity().registerReceiver(receiver, intentFilter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        binding.btnDiscover.setOnClickListener {
            discoverPeers()
        }


        binding.btnGoFight.setOnClickListener {
            showHostDialog()
        }
    }

    private fun showHostDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Hosting")
        builder.setMessage("¿Are you going to be the host?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            findNavController().navigate(
                ConnectionFragmentDirections.actionConnectionFragmentToFightP2PFragment(
                    true,
                    ""
                )
            )
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            showIpDialog()
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showIpDialog() {
        val inputEditText = EditText(requireContext())

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Enter Text")
            .setMessage("Please enter your text below:")
            .setView(inputEditText)
            .setPositiveButton("OK") { dialog, _ ->
                val ipAddress = inputEditText.text.toString()

                findNavController().navigate(
                    ConnectionFragmentDirections.actionConnectionFragmentToFightP2PFragment(
                        false,
                        ipAddress
                    )
                )
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun setupAdapter() {
        adapter = PeersAdapter(peers, this)

        val listManager = LinearLayoutManager(requireContext())

        with(binding) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = listManager
            recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(receiver)
    }

    @SuppressLint("MissingPermission")
    private fun discoverPeers() {
        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(requireContext(), "Discovery Initiated", Toast.LENGTH_SHORT).show()
                //manager.requestPeers(channel, this@ConnectionFragment)
            }

            override fun onFailure(reasonCode: Int) {
                Toast.makeText(
                    requireContext(),
                    "Discovery Failed: $reasonCode",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onPeersAvailable(peerList: WifiP2pDeviceList) {
        Toast.makeText(
            requireContext(),
            peerList.deviceList.count().toString(),
            Toast.LENGTH_SHORT
        ).show()
        /*peers.clear()
        peers.addAll(peerList.deviceList)*/
        adapter.refreshList(peerList)
        // For simplicity, connect to the first device in the list
        //Me llevo el siguiente código a la función de debajo, para que conecte al hacer click
        /*if (peers.isNotEmpty()) {
            val device = peers[0]
            val config = WifiP2pConfig()
            config.deviceAddress = device.deviceAddress

            manager.connect(channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Toast.makeText(requireContext(), "Device connected", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(reason: Int) {
                    Toast.makeText(requireContext(), "Connection failed", Toast.LENGTH_SHORT).show()
                }
            })
        }*/
    }

    @SuppressLint("MissingPermission")
    override fun onDeviceClick(device: WifiP2pDevice) {
        //Toast.makeText(requireContext(),device.deviceName, Toast.LENGTH_LONG).show()
        val config = WifiP2pConfig()
        config.deviceAddress = device.deviceAddress

        manager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(requireContext(), "Device connected", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(reason: Int) {
                Toast.makeText(requireContext(), "Connection failed", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setupIntentFilter() {
        // Indicates a change in the Wi-Fi Direct status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        // Indicates the state of Wi-Fi Direct connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }
}