package com.example.heroes_fight.ui.fight_p2p_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.heroes_fight.ui.fight_fragment.FightFragment
import com.example.heroes_fight.ui.fight_fragment.FightFragmentUiState
import kotlinx.coroutines.launch

class FightP2PFragment : FightFragment() {

    //private val viewModel: FightFragmentViewModel by viewModels()
    override val viewModel: FightP2PFragmentViewModel by viewModels()
    private val args: FightP2PFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.establishConnection(args.isServer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeConnection()
    }

    private fun observeConnection() {
        lifecycleScope.launch {
            viewModel.connectionEstablished.collect {
                if (it) {
                    viewModel.getRandomHeroes()
                }
            }
        }
    }

    override fun setupFightersInSameDevice() {}

    override fun putFightersInTheirPlaces(fightFragmentUiState: FightFragmentUiState.Success) {
        super.putFightersInTheirPlaces(fightFragmentUiState)
        if (args.isServer) {
            viewModel.sendFighters()
        }
    }
}