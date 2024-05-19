package com.example.heroes_fight.ui.fight_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetFighterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FightFragmentViewModel @Inject constructor(
    private val getFighterByIdUseCase: GetFighterByIdUseCase
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


    private val heroesList = ArrayList<FighterModel>()
    private val villainList = ArrayList<FighterModel>()
    private val allFightersList = ArrayList<FighterModel>()


    fun getRandomHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(FightFragmentUiState.Loading)
            Log.i("quique", "COGE LA PRIMERA LISTA")
            addHeroesToStartList()

            Log.i("quique", "COGE LA SEGUNDA LISTA")
            addVillainsToStartList()

            Log.i("quique", "METE TODOS LOS FIGHTERS EN LA LISTA COMÚN")
            orderFightersBySpeed()

            Log.i("quique", "EMITE LAS LISTAS")
            _uiState.emit(
                FightFragmentUiState.Success(
                    heroesList,
                    villainList
                )
            )

            _actualFighter.emit(allFightersList[0])

//            if (baseResponseForHero is BaseResponse.Success && baseResponseForVillain is BaseResponse.Success) {
//
//                // TODO: dejo aquí hardcodeada la posición de los fighters
//                baseResponseForHero.data.position.y = 0
//                baseResponseForHero.data.position.x = 0
//                baseResponseForVillain.data.position.y = 9
//                baseResponseForVillain.data.position.x = 8
//
//                heroesList.add(baseResponseForHero.data)
//                villainList.add(baseResponseForVillain.data)
//
//                orderFightersBySpeed()
//
//                _uiState.emit(
//                    FightFragmentUiState.Success(
//                        heroesList,
//                        villainList
//                    )
//                )
//
//                _actualFighter.emit(allFightersList[0])
//
//            } else {
//                _uiState.emit(FightFragmentUiState.Error(ErrorModel()))
//            }
        }
    }

    private suspend fun addVillainsToStartList() {
        var startXPosition = 8
        for (i in 0..4) {
            Log.i("quique", "vuelta nº $i")
            val baseResponseForVillain = getFighterByIdUseCase(Random.nextInt(1, 732))
            if (baseResponseForVillain is BaseResponse.Success) {
                villainList.add(baseResponseForVillain.data)
                villainList[i].position = Position(9, startXPosition)
                startXPosition--
            }
        }

    }

    private suspend fun addHeroesToStartList() {
        for (i in 0..4) {
            Log.i("quique", "vuelta nº $i")
            val baseResponseForHero = getFighterByIdUseCase(Random.nextInt(1, 732))
            if (baseResponseForHero is BaseResponse.Success)
                heroesList.add(baseResponseForHero.data)
            heroesList[i].position = Position(0, i)
        }

    }

    private fun orderFightersBySpeed() {
        allFightersList.addAll(heroesList)
        allFightersList.addAll(villainList)
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

                viewModelScope.launch {
                    _dyingFighter.emit(enemyToAttack)
                }
                removeDeadFighterFromLists(enemyToAttack)
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

    private fun removeDeadFighterFromLists(enemyToAttack: FighterModel) {
        if (villainList.contains(enemyToAttack)) {
            villainList.remove(enemyToAttack)
        } else {
            heroesList.remove(enemyToAttack)
        }
        allFightersList.remove(enemyToAttack)
    }

    fun performDefense() {
        val resultOfDefense = _actualFighter.value.defense()
        viewModelScope.launch {
            _actionResult.emit(resultOfDefense)
        }
    }
}