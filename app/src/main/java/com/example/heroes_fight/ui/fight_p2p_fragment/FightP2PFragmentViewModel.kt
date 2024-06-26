package com.example.heroes_fight.ui.fight_p2p_fragment

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.common.ResultToSendBySocketModel
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.use_case.GetHeroesListUseCase
import com.example.heroes_fight.data.domain.use_case.GetVillainListUseCase
import com.example.heroes_fight.data.utils.BoardManager
import com.example.heroes_fight.ui.fight_fragment.FightFragmentUiState
import com.example.heroes_fight.ui.fight_fragment.FightFragmentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FightP2PFragmentViewModel @Inject constructor(
    private val getVillainListUseCase: GetVillainListUseCase,
    private val getHeroesListUseCase: GetHeroesListUseCase,
    private val boardManager: BoardManager
) : FightFragmentViewModel(getVillainListUseCase, getHeroesListUseCase, boardManager) {

    private val _connectionEstablished = MutableStateFlow(false)
    val connectionEstablished: StateFlow<Boolean> = _connectionEstablished

    private val _rocksFlow = MutableSharedFlow<MutableList<RockModel>>()
    val rocksFlow: SharedFlow<MutableList<RockModel>> = _rocksFlow

    private lateinit var server: TcpServer
    private lateinit var client: TcpClient
    private var isServer = false

    private val _actionFromOtherDevice = MutableSharedFlow<ResultToSendBySocketModel>()
    val actionFromOtherDevice: SharedFlow<ResultToSendBySocketModel> = _actionFromOtherDevice

    fun establishConnection(isServer: Boolean) {
        this.isServer = isServer
        viewModelScope.launch {
            val deferred = async {
                if (isServer) {
                    Log.i("skts", "Ha entrado en la parte de server")
                    server = TcpServer(8888)
                    server.startServer()
                } else {
                    Log.i("skts", "Ha entrado en la parte de cliente")
                    client = TcpClient(
                        "192.168.1.140",
                        8888
                    ) // "10.0.2.2" es la dirección IP del host para los emuladores
                    client.connectToServer()
                }
            }
            deferred.await()
            Log.i("skts", "Ha dejado de esperar")
            _connectionEstablished.emit(true)
        }

    }

    override fun getRandomHeroes() {
        if (isServer) {
            super.getRandomHeroes()
        } else {
            viewModelScope.launch {

                val deferred = async {
                    client.getFightersList(heroes, villains, allFighters)
                }


                deferred.await()

                Log.i("skts", "Emite el uiState")
                Log.i("skts", "Nº héroes ${heroes.size}")
                Log.i("skts", "Nº villanos ${villains.size}")
                Log.i("skts", "Nº total ${allFighters.size}")
                _uiState.emit(
                    FightFragmentUiState.Success(
                        heroes,
                        villains,
                        allFighters
                    )
                )
                Log.i("skts", "Emite el fighter actual -> ${allFighters[0].id}")
                _actualFighter.emit(allFighters[0])
            }
        }
    }

    fun sendFighters() {
        Log.i("skts", "Ha entrado en la función de sendFighters")
        server.sendFightersList(heroes, villains, allFighters)
    }

    fun sendRocksToServer(rocks: MutableList<RockModel>) {
        viewModelScope.launch {
            val deferred = async { client.sendRocks(rocks) }
            deferred.await()
            //chooseWhoWaitForActions()
        }
    }

    fun chooseWhoWaitForActions() {
        Log.i("skts", "Ha entrado en chooseWhoWaitForActions")
        if (actualFighter.value.isHero && !isServer) {
            clientAwaitForActions()
        } else if (!actualFighter.value.isHero && isServer) {
            serverAwaitForActions()
        }
    }

    suspend fun getRocksFromClient(rocks: MutableList<RockModel>) {
        Log.i("skts", "Intenta coger la lista de rocas")
        viewModelScope.launch {
            val deferred = async { server.getRocksFromClient(rocks) }
            deferred.await()
            _rocksFlow.emit(rocks)
            Log.i("skts", "Ha terminado de esperar por las rocas")
            //chooseWhoWaitForActions()
        }
    }

    fun sendMovement() {
        if (isServer) {
            server.sendMove(_actualFighter.value)
        } else {
            client.sendMove(_actualFighter.value)
        }
    }


    fun clientAwaitForActions() {
        Log.i("skts", "cliente esperando por acciones")
        viewModelScope.launch {
            client.awaitForEnemyActions(_actionFromOtherDevice, villains, heroes)
        }
    }

    fun serverAwaitForActions() {
        Log.i("skts", "servidor esperando por acciones")
        viewModelScope.launch {
            server.awaitForEnemyActions(_actionFromOtherDevice, villains, heroes)
        }
    }

    override fun finishTurn() {
        viewModelScope.launch(Dispatchers.IO) {
            val deferred = async {
                if (_actualFighter.value.isHero && isServer) {
                    Log.i("skts", "enviando finnish turn desde server")
                    server.sendFinnishTurn()
                } else if (!_actualFighter.value.isHero && !isServer) {
                    Log.i("skts", "enviando finish turn desde cliente")
                    client.sendFinnishTurn()
                }
            }
            Log.i("skts", "espera a terminar el envío de finnishTurn")
            deferred.await()

            super.finishTurn()

        }
    }

    override fun performDefense() {
        val resultOfDefense = _actualFighter.value.defense()
        viewModelScope.launch {
            _actionResult.emit(resultOfDefense)
        }
        if (isServer) {
            server.sendDefense(_actualFighter.value, resultOfDefense)
        } else {
            client.sendDefense(_actualFighter.value, resultOfDefense)
        }
    }

    override fun performSupport(allyToSupport: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfSupport = _actualFighter.value.support(allyToSupport)
            viewModelScope.launch {
                _actionResult.emit(resultOfSupport)
            }

            if (_actualFighter.value.actionPerformed) {
                if (isServer) {
                    server.sendSupport(allyToSupport, resultOfSupport)
                } else {
                    client.sendSupport(allyToSupport, resultOfSupport)
                }
            }
        }
    }
    override fun performSabotage(enemyToSabotage: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfSabotage = _actualFighter.value.sabotage(enemyToSabotage)
            viewModelScope.launch {
                _actionResult.emit(resultOfSabotage)
            }
            if (_actualFighter.value.actionPerformed) {
                if (isServer) {
                    server.sendSabotage(enemyToSabotage, resultOfSabotage)
                } else {
                    client.sendSabotage(enemyToSabotage, resultOfSabotage)
                }
            }

        }
    }
}