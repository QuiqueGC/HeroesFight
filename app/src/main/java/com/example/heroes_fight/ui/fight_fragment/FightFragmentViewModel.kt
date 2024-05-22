package com.example.heroes_fight.ui.fight_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.use_case.GetHeroesListUseCase
import com.example.heroes_fight.data.domain.use_case.GetVillainListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FightFragmentViewModel @Inject constructor(
    private val getVillainListUseCase: GetVillainListUseCase,
    private val getHeroesListUseCase: GetHeroesListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FightFragmentUiState>(FightFragmentUiState.Loading)
    val uiState: StateFlow<FightFragmentUiState> = _uiState

    private val _actualFighter = MutableStateFlow(FighterModel())
    val actualFighter: StateFlow<FighterModel> = _actualFighter

    private val _fighterMovement = MutableSharedFlow<Boolean>()
    val fighterMovement: SharedFlow<Boolean> = _fighterMovement

    private val _actionResult = MutableSharedFlow<String>()
    val actionResult: SharedFlow<String> = _actionResult

    private val _dyingFighter = MutableSharedFlow<FighterModel>()
    val dyingFighter: SharedFlow<FighterModel> = _dyingFighter


    private var heroesList = ArrayList<FighterModel>()
    private var villainList = ArrayList<FighterModel>()
    private var allFightersList = ArrayList<FighterModel>()


    fun getRandomHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(FightFragmentUiState.Loading)

            do {
                var repeatedFighter = false
                allFightersList = ArrayList()

                Log.i("quique", "COGE LA PRIMERA LISTA")
                addHeroesToStartList()

                Log.i("quique", "COGE LA SEGUNDA LISTA")
                addVillainsToStartList()

                Log.i("quique", "METE TODOS LOS FIGHTERS EN LA LISTA COMÚN")
                orderFightersBySpeed()

                if (allFightersList.groupBy { it.id }.count() < 10) {
                    repeatedFighter = true
                }

            } while (repeatedFighter)


            Log.i("quique", "EMITE LAS LISTAS")
            _uiState.emit(
                FightFragmentUiState.Success(
                    heroesList,
                    villainList,
                    allFightersList
                )
            )

            _actualFighter.emit(allFightersList[0])
        }
    }

    private suspend fun addVillainsToStartList() {
        villainList = getVillainListUseCase()
    }

    private suspend fun addHeroesToStartList() {
        heroesList = getHeroesListUseCase()
    }

    private fun orderFightersBySpeed() {
        allFightersList.addAll(heroesList)
        allFightersList.addAll(villainList)
        allFightersList.removeAll { it.durability <= 0 }
        allFightersList.sortByDescending { it.speed }
    }

    fun finishTurn() {
        _actualFighter.value.refreshDataToNextTurn()
        allFightersList.removeFirst()
        if (allFightersList.isEmpty()) {
            orderFightersBySpeed()
        }

        viewModelScope.launch {
            _actualFighter.emit(allFightersList[0])
            _actualFighter.value.removeDefenseBonus()
        }
    }

    fun tryToMoveFighter(destinationPosition: Position) {
        if (!_actualFighter.value.movementPerformed) {
            viewModelScope.launch {
                _fighterMovement.emit(_actualFighter.value.move(destinationPosition))
            }
        }
    }

    fun performAttack(enemyToAttack: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfAttack = _actualFighter.value.attack(enemyToAttack)
            viewModelScope.launch {
                _actionResult.emit(resultOfAttack)
            }

            if (enemyToAttack.durability <= 0) {
                allFightersList.remove(enemyToAttack)
                viewModelScope.launch {
                    _dyingFighter.emit(enemyToAttack)
                }
            }
        }

    }

    fun performSabotage(enemyToSabotage: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfSabotage = _actualFighter.value.sabotage(enemyToSabotage)
            viewModelScope.launch {
                _actionResult.emit(resultOfSabotage)
            }
        }
    }

    fun performSupport(allyToSupport: FighterModel) {
        if (!_actualFighter.value.actionPerformed) {
            val resultOfSabotage = _actualFighter.value.support(allyToSupport)
            viewModelScope.launch {
                _actionResult.emit(resultOfSabotage)
            }
        }
    }

    fun performDefense() {
        val resultOfDefense = _actualFighter.value.defense()
        viewModelScope.launch {
            _actionResult.emit(resultOfDefense)
        }
    }
}