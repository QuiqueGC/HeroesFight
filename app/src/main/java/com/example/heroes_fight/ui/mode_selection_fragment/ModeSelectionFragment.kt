package com.example.heroes_fight.ui.mode_selection_fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.heroes_fight.databinding.DialogHostingBinding
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
                setupConnectionDialogAndShow()
            }
        }
    }

    private fun setupConnectionDialogAndShow() {
        val dialogBinding = DialogHostingBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)

        dialogBinding.btnHost.setOnClickListener {
            findNavController().navigate(
                ModeSelectionFragmentDirections.actionModeSelectionFragmentToFightP2PFragment(
                    true,
                    ""
                )
            )
            dialog.dismiss()
        }

        dialogBinding.btnClient.setOnClickListener {
            dialogBinding.etIpAddress.visibility = View.VISIBLE
            dialogBinding.btnConnectClient.visibility = View.VISIBLE
        }

        dialogBinding.btnConnectClient.setOnClickListener {

            val ipAddress = dialogBinding.etIpAddress.text.toString()
            findNavController().navigate(
                ModeSelectionFragmentDirections.actionModeSelectionFragmentToFightP2PFragment(
                    false,
                    ipAddress
                )
            )
            dialog.dismiss()
        }
        dialog.show()
    }
}