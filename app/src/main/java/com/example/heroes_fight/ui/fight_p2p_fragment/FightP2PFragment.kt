package com.example.heroes_fight.ui.fight_p2p_fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.heroes_fight.ui.fight_fragment.FightFragment
import kotlinx.coroutines.launch

class FightP2PFragment : FightFragment() {

    override val viewModel: FightP2PFragmentViewModel by viewModels()
    private val args: FightP2PFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.establishConnection(args.isServer, args.ipAddress)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeConnection()

        observeRocksFlow()

        observeResultEnemyAction()

    }
    private fun observeRocksFlow() {
        lifecycleScope.launch {
            viewModel.rocksFlow.collect {
                showRocks(it)
            }
        }
    }

    private fun observeResultEnemyAction() {
        lifecycleScope.launch {
            viewModel.actionFromOtherDevice.collect {

                when (it.action) {
                    "move" -> {
                        destinationPosition = actualFighter.position
                        Log.i(
                            "skts",
                            "Posición de actualFighter ->>> " + actualFighter.position.y + "," + actualFighter.position.x
                        )
                        moveFighterView()
                    }

                    "pass" -> {
                        Log.i("skts", "EMPIEZA TODO LO DEL FINNISH_TURN PORQUE HA RECIBIDO PASS")
                        finishTurn()
                    }

                    "defense" -> {
                        showInfo(it)
                        startTimerToHideTvResult()
                    }

                    "support" -> {
                        showInfo(it)
                        startTimerToHideTvResult()
                    }

                    "sabotage" -> {
                        showInfo(it)
                        startTimerToHideTvResult()
                    }

                    "attack" -> {
                        showInfo(it)
                        startTimerToHideTvResult()
                        viewModel.checkDeadsFromOtherDevice()
                    }
                }

            }
        }
    }

    override fun moveFighterView() {
        super.moveFighterView()
        if (actualFighter.isHero && args.isServer || !actualFighter.isHero && !args.isServer) {
            Log.i("skts", "Ha entrado en el sendMovement para enviar al otro dispositivo")
            viewModel.sendMovement()
        }
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

    override fun collectDyingFighter() {
        lifecycleScope.launch {
            viewModel.dyingFighter.collect { dyingFighter ->
                val indexOfDyingFighter: Int
                if (heroes.any { it.id == dyingFighter.id }) {
                    indexOfDyingFighter = heroes.indexOfFirst { it.id == dyingFighter.id }
                    ivHeroes[indexOfDyingFighter].visibility = View.GONE

                    showReferee(
                        "${actualFighter.name} has killed " + heroes[indexOfDyingFighter].name + "!!!"
                    )

                } else {
                    indexOfDyingFighter = villains.indexOfFirst { it.id == dyingFighter.id }
                    ivVillains[indexOfDyingFighter].visibility = View.GONE
                    showReferee(
                        "${actualFighter.name} has killed " + villains[indexOfDyingFighter].name + "!!!"
                    )
                }
                removeOfInitiativeList(dyingFighter)
            }
        }
    }

    override fun collectFinishBattle() {
        lifecycleScope.launch {
            viewModel.finishBattle.collect { scoreListModel ->
                if (scoreListModel != null) {
                    if (heroes.none { it.score.survived } && args.isServer ||
                        villains.none { it.score.survived } && !args.isServer
                    ) {
                        findNavController().navigate(
                            FightP2PFragmentDirections.actionFightP2PFragmentToScoreP2PFragment(
                                false,
                                scoreListModel
                            )
                        )
                    } else {
                        findNavController().navigate(
                            FightP2PFragmentDirections.actionFightP2PFragmentToScoreP2PFragment(
                                true,
                                scoreListModel
                            )
                        )
                    }
                }
            }
        }
    }

    override fun setupFightersInSameDevice() {}

    override fun setupRocks() {
        if (args.isServer) {
            super.setupRocks()
        }
    }

    override fun collectActualFighter() {
        super.collectActualFighter()
        Log.i("skts", "Ha terminado de hacer lo básico de emitir actualFighter")
        Log.i("skts", "ID del actualFighter = ${actualFighter.id}")
        establishWhoIsThePlayer()
    }

    private fun establishWhoIsThePlayer() {
        lifecycleScope.launch {
            viewModel.actualFighter.collect {
                if (actualFighter.id != 0) {
                    if (actualFighter.isHero && args.isServer || !actualFighter.isHero && !args.isServer) {

                        if (!actualFighter.isSabotaged) {
                            actionButtons.forEach {
                                it.isEnabled = true
                                it.visibility = View.VISIBLE
                            }
                        }
                        binding.btnPass.visibility = View.VISIBLE
                        binding.btnPass.isEnabled = true

                    } else {
                        if (!actualFighter.isSabotaged) {
                            actionButtons.forEach {
                                it.isEnabled = false
                                it.visibility = View.GONE
                            }
                        }
                        binding.btnPass.visibility = View.GONE
                        binding.btnPass.isEnabled = false
                    }

                    viewModel.chooseWhoWaitForActions()
                }
            }
        }
    }

    override fun showAllViews() {
        super.showAllViews()
        if (args.isServer) {
            viewModel.sendDataToFight(rocks)
        }
    }
}