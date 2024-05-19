package com.example.heroes_fight.ui.fight_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetHeroByIdUseCase
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
    private val getHeroByIdUseCase: GetHeroByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FightFragmentUiState>(FightFragmentUiState.Loading)
    val uiState: StateFlow<FightFragmentUiState> = _uiState

    private val _actualFighter = MutableStateFlow(HeroModel())
    val actualFighter: StateFlow<HeroModel> = _actualFighter

    private val _fighterCanMove = MutableSharedFlow<Boolean>()
    val fighterCanMove: SharedFlow<Boolean> = _fighterCanMove

    private val heroesList = ArrayList<HeroModel>()
    private val villainList = ArrayList<HeroModel>()
    private val allFightersList = ArrayList<HeroModel>()


    fun getRandomHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(FightFragmentUiState.Loading)
            val baseResponseForHero = getHeroByIdUseCase(Random.nextInt(1, 732))
            val baseResponseForVillain = getHeroByIdUseCase(Random.nextInt(1, 732))

            if (baseResponseForHero is BaseResponse.Success && baseResponseForVillain is BaseResponse.Success) {

                // TODO: dejo aquí hardcodeada la posición de los fighters
                baseResponseForHero.data.position.y = 0
                baseResponseForHero.data.position.x = 0
                baseResponseForVillain.data.position.y = 9
                baseResponseForVillain.data.position.x = 9

                heroesList.add(baseResponseForHero.data)
                villainList.add(baseResponseForVillain.data)

                orderFightersBySpeed()

                _uiState.emit(
                    FightFragmentUiState.Success(
                        heroesList,
                        villainList
                    )
                )

                _actualFighter.emit(allFightersList[0])

            } else {
                _uiState.emit(FightFragmentUiState.Error(ErrorModel()))
            }
        }
    }

    private fun orderFightersBySpeed() {
        allFightersList.addAll(heroesList)
        allFightersList.addAll(villainList)
        allFightersList.sortByDescending { it.speed }
    }

    fun finishTurn() {
        allFightersList.removeFirst()
        if (allFightersList.isEmpty()) {
            orderFightersBySpeed()
        }
        viewModelScope.launch {
            _actualFighter.emit(allFightersList[0])
        }
    }

    fun tryToMoveFighter(position: Position) {
        val originValue = _actualFighter.value.position.y + _actualFighter.value.position.x
        val destinationValue = position.y + position.x
        val speed = if (_actualFighter.value.speed / 10 < 1) {
            1
        } else {
            _actualFighter.value.speed / 10
        }

        viewModelScope.launch {
            if (originValue < destinationValue) {
                Log.i("quique", "Posición destino ---> ${destinationValue}")
                Log.i("quique", "Posición origen ---> ${originValue}")
                Log.i("quique", "Speed ---> ${speed}")
                if (originValue + speed >= destinationValue) {
                    _fighterCanMove.emit(true)
                    _actualFighter.value.position = Position(position.y, position.x)
                    Log.i(
                        "quique",
                        "Posición y del que está en el flow ---> ${_actualFighter.value.position.y}"
                    )
                    Log.i("quique", "Posición y del otro --->  ${allFightersList[0].position.y}")
                } else {
                    _fighterCanMove.emit(false)
                }
            } else {
                Log.i("quique", "Posición destino ---> ${destinationValue}")
                Log.i("quique", "Posición origen ---> ${originValue}")
                Log.i("quique", "Speed ---> ${speed}")
                //no termino de entender por qué tengo que ponerle el +1 si en el caso
                //positivo funciona perfectamente sin añadirle o quitarle (algo se me escapa...)
                if (originValue - speed <= destinationValue + 1) {
                    _fighterCanMove.emit(true)
                    _actualFighter.value.position = Position(position.y, position.x)
                    Log.i(
                        "quique",
                        "Posición y del que está en el flow ---> ${_actualFighter.value.position.y}"
                    )
                    Log.i("quique", "Posición y del otro --->  ${allFightersList[0].position.y}")
                } else {
                    _fighterCanMove.emit(false)
                }
            }
        }


    }
}