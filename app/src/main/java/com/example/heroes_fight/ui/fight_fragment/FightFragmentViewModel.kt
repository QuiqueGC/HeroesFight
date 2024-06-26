package com.example.heroes_fight.ui.fight_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.constants.MyConstants
import com.example.heroes_fight.data.domain.model.common.ActionResultModel
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.model.fighter.ScoreListModel
import com.example.heroes_fight.data.domain.model.fighter.ScoreModel
import com.example.heroes_fight.data.domain.use_case.GetHeroesListUseCase
import com.example.heroes_fight.data.domain.use_case.GetVillainListUseCase
import com.example.heroes_fight.data.utils.BoardManager
import com.example.heroes_fight.data.utils.PlayerChoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
open class FightFragmentViewModel @Inject constructor(
    private val getVillainListUseCase: GetVillainListUseCase,
    private val getHeroesListUseCase: GetHeroesListUseCase,
    private val boardManager: BoardManager
) : ViewModel() {

    open val _uiState = MutableStateFlow<FightFragmentUiState>(FightFragmentUiState.Loading)
    val uiState: StateFlow<FightFragmentUiState> = _uiState

    open val _actualFighter = MutableStateFlow(FighterModel())
    val actualFighter: StateFlow<FighterModel> = _actualFighter

    open val _actionResult = MutableSharedFlow<ActionResultModel>()
    val actionResult: SharedFlow<ActionResultModel> = _actionResult

    open val _dyingFighter = MutableSharedFlow<FighterModel>()
    val dyingFighter: SharedFlow<FighterModel> = _dyingFighter

    open val _finishBattle = MutableStateFlow<ScoreListModel?>(null)
    val finishBattle: StateFlow<ScoreListModel?> = _finishBattle


    open var heroes = mutableListOf<FighterModel>()
    open var villains = mutableListOf<FighterModel>()
    open var allFighters = mutableListOf<FighterModel>()


    open fun getRandomHeroes() {
        var isBalanced = false
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(FightFragmentUiState.Loading)
            do {
                var repeatedFighter = false
                allFighters = mutableListOf()

                Log.i("quique", "COGE LA PRIMERA LISTA")
                addHeroesToStartList()

                Log.i("quique", "COGE LA SEGUNDA LISTA")
                addVillainsToStartList()

                Log.i("quique", "METE TODOS LOS FIGHTERS EN LA LISTA COMÚN")
                orderFightersBySpeed()
                Log.i("quique", "VALORES CALCULADOS")
                val powerHeroes = getAllPower(heroes)
                Log.i("quique", "Valor de héroes -> $powerHeroes")
                val powerVillains = getAllPower(villains)
                Log.i("quique", "Valor de villanos -> $powerVillains")
                val powerDifference = abs(powerHeroes - powerVillains)
                Log.i("quique", "Diferencia -> $powerDifference")
                if (powerDifference <= 150) {
                    isBalanced = true
                }

                if (allFighters.groupBy { it.id }.count() < 10) {
                    repeatedFighter = true
                }

            } while (repeatedFighter || !isBalanced)

            Log.i("quique", "YA SALIÓ DEL BUCLE. VER LOS DOS ÚLTIMOS VALORES")

            Log.i("quique", "EMITE LAS LISTAS")
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

    private fun getAllPower(heroesList: List<FighterModel>): Int {
        var totalPowerStats = 0
        for (fighter in heroesList) {
            with(fighter) {
                totalPowerStats += intelligence + speed + durability + strength + combat + power
            }
        }
        return totalPowerStats
    }

    private suspend fun addVillainsToStartList() {
        villains = getVillainListUseCase()
    }

    private suspend fun addHeroesToStartList() {
        heroes = getHeroesListUseCase()
    }

    private fun orderFightersBySpeed() {
        allFighters.addAll(heroes)
        allFighters.addAll(villains)
        allFighters.removeAll { it.durability <= 0 }
        allFighters.sortByDescending { it.speed }
    }

    open fun finishTurn() {
        Log.i("skts", "AQUÍ EMPIEZA LA FUNCIÓN PRINCIPAL DE FINISH_TURN (LA DEL SUPER)")
        _actualFighter.value.refreshDataToNextTurn()
        Log.i("skts", "la lista antes de remover el primero (cantidad) ${allFighters.size}")

        allFighters.removeFirst()
        if (allFighters.isEmpty()) {
            orderFightersBySpeed()
        }
        Log.i("skts", "la lista después de remover el primero (cantidad) ${allFighters.size}")

        viewModelScope.launch(Dispatchers.IO) {
            Log.i("skts", "emite el actual fighter")

            _actualFighter.emit(allFighters.first())

            _actualFighter.value.removeDefenseBonus()

        }
        Log.i("skts", "AQUÍ TERMINA EL SÚPER")
    }

    fun performMovement(destinationPosition: Position): Boolean {
        return if (!_actualFighter.value.movementPerformed) {
            _actualFighter.value.move(destinationPosition)
        } else {
            false
        }
    }

    open fun performAttack(enemyToAttack: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfAttack = _actualFighter.value.attack(enemyToAttack)
            viewModelScope.launch {
                _actionResult.emit(resultOfAttack)
            }

            if (enemyToAttack.durability <= 0) {
                _actualFighter.value.score.kills++
                enemyToAttack.score.survived = false

                checkIfFinishGameOrJustDie(enemyToAttack)
            }
        }
    }

    fun performShot(enemyToAttack: FighterModel, rocks: List<RockModel>) {
        val allFightersToCheck = mutableListOf<FighterModel>()
        allFightersToCheck.addAll(heroes)
        allFightersToCheck.addAll(villains)
        allFightersToCheck.removeAll { it.durability <= 0 }
        if (!_actualFighter.value.actionPerformed) {
            val resultOfShot = _actualFighter.value.shot(enemyToAttack, rocks, allFightersToCheck)

            viewModelScope.launch {
                _actionResult.emit(resultOfShot)
            }

            if (enemyToAttack.durability <= 0) {
                _actualFighter.value.score.kills++
                enemyToAttack.score.survived = false

                checkIfFinishGameOrJustDie(enemyToAttack)
            }
        }
    }

    open fun checkIfFinishGameOrJustDie(enemyToAttack: FighterModel) {
        if (heroes.none { it.score.survived }) {
            val scores = mutableListOf<ScoreModel>()
            villains.forEach { scores.add(it.score) }
            viewModelScope.launch {
                _finishBattle.emit(ScoreListModel(false, scores))
            }
        } else if (villains.none { it.score.survived }) {
            val scores = mutableListOf<ScoreModel>()
            heroes.forEach { scores.add(it.score) }
            viewModelScope.launch {
                _finishBattle.emit(ScoreListModel(true, scores))
            }
        } else {
            allFighters.remove(enemyToAttack)
            viewModelScope.launch {
                _dyingFighter.emit(enemyToAttack)
            }
        }
    }

    open fun performSabotage(enemyToSabotage: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfSabotage = _actualFighter.value.sabotage(enemyToSabotage)
            viewModelScope.launch {
                _actionResult.emit(resultOfSabotage)
            }
        }
    }

    open fun performSupport(allyToSupport: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfSupport = _actualFighter.value.support(allyToSupport)
            viewModelScope.launch {
                _actionResult.emit(resultOfSupport)
            }
        }
    }

    open fun performDefense() {
        val resultOfDefense = _actualFighter.value.defense()
        viewModelScope.launch {
            _actionResult.emit(resultOfDefense)
        }
    }

    fun getAccessibleTiles(playerChoice: PlayerChoice): Array<Array<Boolean?>> {
        return when (playerChoice) {
            PlayerChoice.MOVE -> boardManager.getAccessibleTiles(
                _actualFighter.value.movementCapacity,
                _actualFighter.value.position
            )

            PlayerChoice.SHOT -> boardManager.getAccessibleTiles(
                _actualFighter.value.distanceToShot,
                _actualFighter.value.position
            )

            else -> boardManager.getAccessibleTiles(
                MyConstants.MELEE_DISTANCE,
                _actualFighter.value.position
            )
        }
    }

    fun getRocks(): MutableList<RockModel> {
        return boardManager.getRocks()
    }
}