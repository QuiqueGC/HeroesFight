package com.example.heroes_fight.ui.mode_selection_fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.heroes_fight.databinding.FragmentModeSelectionBinding


class ModeSelectionFragment : Fragment() {

    private lateinit var binding: FragmentModeSelectionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModeSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSameDevice.setOnClickListener {
                findNavController().navigate(
                    ModeSelectionFragmentDirections.actionModeSelectionFragmentToFightFragment()
                )
            }
            btnWifiDirect.setOnClickListener {
                checkWifiDirectPermissions()
            }
        }
    }


    private fun checkWifiDirectPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestWifiDirectPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.NEARBY_WIFI_DEVICES
                )
            )
        } else {
            requestWifiDirectPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE
                )
            )
        }
    }

    private val requestWifiDirectPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val accessFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val accessCoarseLocation =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            val accessWifiState = permissions[Manifest.permission.ACCESS_WIFI_STATE] ?: false
            val changeWifiState = permissions[Manifest.permission.CHANGE_WIFI_STATE] ?: false
            val permissionsDenied: Boolean

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                val nearbyWifiDevices =
                    permissions[Manifest.permission.NEARBY_WIFI_DEVICES] ?: false

                permissionsDenied = !accessFineLocation ||
                        !accessCoarseLocation ||
                        !accessWifiState ||
                        !changeWifiState ||
                        !nearbyWifiDevices
            } else {

                permissionsDenied = !accessFineLocation ||
                        !accessCoarseLocation ||
                        !accessWifiState ||
                        !changeWifiState
            }

            if (permissionsDenied) {
                Toast.makeText(
                    requireContext(),
                    "This mode cannot be accessed without accepting permissions",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(ModeSelectionFragmentDirections.actionModeSelectionFragmentToConnectionFragment())
            }
        }
}