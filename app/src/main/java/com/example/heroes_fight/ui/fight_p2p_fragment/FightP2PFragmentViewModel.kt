package com.example.heroes_fight.ui.fight_p2p_fragment

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.common.ResultToSendBySocketModel
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.model.fighter.ScoreListModel
import com.example.heroes_fight.data.domain.model.fighter.ScoreModel
import com.example.heroes_fight.data.domain.repository.tcp_ip.TcpClient
import com.example.heroes_fight.data.domain.repository.tcp_ip.TcpServer
import com.example.heroes_fight.data.domain.use_case.database.GetHeroesListFromDBUseCase
import com.example.heroes_fight.data.domain.use_case.database.GetVillainListFromDBUseCase
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
    getVillainListFromDBUseCase: GetVillainListFromDBUseCase,
    getHeroesListFromDBUseCase: GetHeroesListFromDBUseCase,
    boardManager: BoardManager
) : FightFragmentViewModel(getVillainListFromDBUseCase, getHeroesListFromDBUseCase, boardManager) {

    private val _connectionEstablished = MutableStateFlow(false)
    val connectionEstablished: StateFlow<Boolean> = _connectionEstablished

    private val _rocksFlow = MutableSharedFlow<MutableList<RockModel>>()
    val rocksFlow: SharedFlow<MutableList<RockModel>> = _rocksFlow

    private lateinit var server: TcpServer
    private lateinit var client: TcpClient
    private var isServer = false
    private val rocks = mutableListOf<RockModel>()

    private val _actionFromOtherDevice = MutableSharedFlow<ResultToSendBySocketModel>()
    val actionFromOtherDevice: SharedFlow<ResultToSendBySocketModel> = _actionFromOtherDevice


    private var attackedEnemy = FighterModel()

    fun establishConnection(isServer: Boolean, ipAddress: String) {
        //"192.168.1.140"
        this.isServer = isServer
        viewModelScope.launch {
            val deferred = async {
                if (isServer) {
                    Log.i("skts", "Ha entrado en la parte de server")
                    server = TcpServer(8888)
                    server.startServer()
                    _connectionEstablished.emit(true)
                } else {
                    Log.i("skts", "Ha entrado en la parte de cliente")
                    client = TcpClient(
                        ipAddress,
                        8888
                    ) // "10.0.2.2" es la dirección IP del host para los emuladores
                    if (client.connectToServer()) {
                        _connectionEstablished.emit(true)
                    } else {
                        _uiState.emit(FightFragmentUiState.Error(ErrorModel(message = "Impossible to connect. Go back and try again")))
                    }
                }
            }
            deferred.await()
            Log.i("skts", "Ha dejado de esperar")
        }
    }
    override fun getRandomHeroes() {
        if (isServer) {
            super.getRandomHeroes()
        } else {
            viewModelScope.launch {
                val deferred = async { client.awaitForData(heroes, villains, allFighters, rocks) }
                deferred.await()
                _rocksFlow.emit(rocks)
                _uiState.emit(
                    FightFragmentUiState.Success(
                        heroes,
                        villains,
                        allFighters
                    )
                )
                _actualFighter.emit(allFighters[0])
            }

        }


        /*else {
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
        }}*/
    }



    fun chooseWhoWaitForActions() {
        Log.i("skts", "Ha entrado en chooseWhoWaitForActions")
        if (actualFighter.value.isHero && !isServer) {
            clientAwaitForActions()
        } else if (!actualFighter.value.isHero && isServer) {
            serverAwaitForActions()
        }
    }


    fun sendMovement() {
        if (isServer) {
            server.sendMove(_actualFighter.value)
        } else {
            client.sendMove(_actualFighter.value)
        }
    }


    private fun clientAwaitForActions() {
        Log.i("skts", "cliente esperando por acciones")
        viewModelScope.launch {
            client.awaitForEnemyActions(_actionFromOtherDevice, heroes, villains, attackedEnemy)
        }
    }

    private fun serverAwaitForActions() {
        Log.i("skts", "servidor esperando por acciones")
        viewModelScope.launch {
            server.awaitForEnemyActions(_actionFromOtherDevice, villains, heroes, attackedEnemy)
        }
    }

    override fun finishTurn() {
        attackedEnemy = FighterModel()
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

    override fun performAttack(enemyToAttack: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfAttack = _actualFighter.value.attack(enemyToAttack)
            viewModelScope.launch {
                _actionResult.emit(resultOfAttack)
            }
            if (_actualFighter.value.actionPerformed) {
                if (isServer) {
                    server.sendAttack(enemyToAttack, resultOfAttack)
                } else {
                    client.sendAttack(enemyToAttack, resultOfAttack)
                }
            }

            if (enemyToAttack.durability <= 0) {
                _actualFighter.value.score.kills++
                enemyToAttack.score.survived = false

                checkIfFinishGameOrJustDie(enemyToAttack)
            }
        }
    }

    override fun performShot(enemyToAttack: FighterModel, rocks: List<RockModel>) {
        val allFightersToCheck = mutableListOf<FighterModel>()
        allFightersToCheck.addAll(heroes)
        allFightersToCheck.addAll(villains)
        allFightersToCheck.removeAll { it.durability <= 0 }
        if (!_actualFighter.value.actionPerformed) {
            val resultOfShot = _actualFighter.value.shot(enemyToAttack, rocks, allFightersToCheck)

            viewModelScope.launch {
                _actionResult.emit(resultOfShot)
            }

            if (_actualFighter.value.actionPerformed) {
                if (isServer) {
                    server.sendAttack(enemyToAttack, resultOfShot)
                } else {
                    client.sendAttack(enemyToAttack, resultOfShot)
                }
            }

            if (enemyToAttack.durability <= 0) {
                _actualFighter.value.score.kills++
                enemyToAttack.score.survived = false

                checkIfFinishGameOrJustDie(enemyToAttack)
            }
        }
    }

    override fun checkIfFinishGameOrJustDie(enemyToAttack: FighterModel) {
        if (heroes.none { it.score.survived } || villains.none { it.score.survived }) {
            val scores = mutableListOf<ScoreModel>()
            if (isServer) {
                heroes.forEach { scores.add(it.score) }
            } else {
                villains.forEach { scores.add(it.score) }
            }
            viewModelScope.launch {
                _finishBattle.emit(ScoreListModel(false, scores))
            }
        } else {
            allFighters.remove(enemyToAttack)
            viewModelScope.launch {
                _dyingFighter.emit(enemyToAttack)
            }
        }
    }

    fun checkDeadsFromOtherDevice() {
        if (heroes.none { it.score.survived } || villains.none { it.score.survived }) {
            val scores = mutableListOf<ScoreModel>()
            if (isServer) {
                heroes.forEach { scores.add(it.score) }
                viewModelScope.launch {
                    _finishBattle.emit(ScoreListModel(false, scores))
                }
            } else {
                villains.forEach { scores.add(it.score) }
                viewModelScope.launch {
                    _finishBattle.emit(ScoreListModel(true, scores))
                }
            }
        } else {
            Log.i("dying", "Survive? ${attackedEnemy.score.survived}")
            Log.i("dying", "Name? ${attackedEnemy.name}")
            Log.i("dying", "Durability? ${attackedEnemy.durability}")
            Log.i("dying", "id? ${attackedEnemy.id}")
            if (!attackedEnemy.score.survived) {
                allFighters.removeAll { it.id == attackedEnemy.id }
                viewModelScope.launch {
                    _dyingFighter.emit(attackedEnemy)
                }
            }

        }
    }

    fun sendDataToFight(rocks: MutableList<RockModel>) {
        server.sendDataToFight(heroes, villains, allFighters, rocks)
    }


}