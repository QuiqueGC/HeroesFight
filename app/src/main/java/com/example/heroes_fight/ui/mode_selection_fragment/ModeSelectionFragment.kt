package com.example.heroes_fight.ui.mode_selection_fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.heroes_fight.databinding.FragmentModeSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModeSelectionFragment : Fragment() {

    private lateinit var binding: FragmentModeSelectionBinding
    private var isWifiDirect = false
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
                isWifiDirect = true
                checkWifiDirectPermissions()
            }

            btnTcpIp.setOnClickListener {
                isWifiDirect = false
                checkWifiDirectPermissions()
            }
        }
    }

    private fun showHostDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Hosting")
        builder.setMessage("¿Are you going to be the host?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            findNavController().navigate(
                ModeSelectionFragmentDirections.actionModeSelectionFragmentToFightP2PFragment(
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
                    ModeSelectionFragmentDirections.actionModeSelectionFragmentToFightP2PFragment(
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
                if (!isWifiDirect) {
                    showHostDialog()
                } else {
                    findNavController().navigate(ModeSelectionFragmentDirections.actionModeSelectionFragmentToConnectionFragment())
                }
            }
        }
}