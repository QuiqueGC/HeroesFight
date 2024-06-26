package com.example.heroes_fight.ui.fight_p2p_fragment

import android.os.Bundle
import android.util.Log
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

    private var rocksWereSent = false
    private var startedBattle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.establishConnection(args.isServer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeConnection()

        observeRocksFlow()

        observeResultEnemyAction()

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
                        if (args.isServer) {
                            viewModel.checkDeadFighters(heroes)
                        } else {
                            viewModel.checkDeadFighters(villains)
                        }

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

    private fun observeRocksFlow() {
        lifecycleScope.launch {
            viewModel.rocksFlow.collect {
                showRocks()
                startedBattle = true
                if (args.isServer) {
                    viewModel.chooseWhoWaitForActions()
                }
            }
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

    override fun setupFightersInSameDevice() {}

    override fun setupRocks() {
        if (!args.isServer) {
            super.setupRocks()
        }
    }

    override fun collectActualFighter() {
        super.collectActualFighter()
        Log.i("skts", "Ha terminado de hacer lo básico de emitir actualFighter")
        Log.i("skts", "ID del actualFighter = ${actualFighter.id}")
        sendOrGetRocks()
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

                    //viewModel.awaitForEnemyActions()
                }
                if (rocksWereSent && startedBattle) {
                    viewModel.chooseWhoWaitForActions()
                }
            }
        }
    }

    private fun sendOrGetRocks() {
        lifecycleScope.launch {
            viewModel.actualFighter.collect {
                if (actualFighter.id != 0 && !rocksWereSent) {
                    if (!args.isServer) {
                        startedBattle = true
                        viewModel.sendRocksToServer(rocks)
                    } else {
                        Log.i("skts", "Se ejecuta la función para coger las rocas")
                        viewModel.getRocksFromClient(rocks)
                        //showRocks()
                    }

                    rocksWereSent = true
                }
            }
        }
    }

    override fun completeBattlefield(fightFragmentUiState: FightFragmentUiState.Success) {
        super.completeBattlefield(fightFragmentUiState)
        if (args.isServer) {
            Log.i("skts", "Ha entrado en el SUccess del UIState")
            viewModel.sendFighters()
        }
    }

}