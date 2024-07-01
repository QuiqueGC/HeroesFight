package com.example.heroes_fight.ui.mode_selection_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.heroes_fight.databinding.FragmentModeSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

            btnTcpIp.setOnClickListener {
                showHostDialog()
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
            .setTitle("IP Address")
            .setMessage("Please enter the IP address:")
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
}