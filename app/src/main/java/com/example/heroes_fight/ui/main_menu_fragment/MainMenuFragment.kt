package com.example.heroes_fight.ui.main_menu_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.heroes_fight.databinding.FragmentMainMenuBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnList.setOnClickListener {
            findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToCardsCollectionFragment())
        }

        binding.btnFight.setOnClickListener {
            findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToModeSelectionFragment())
        }
    }
}