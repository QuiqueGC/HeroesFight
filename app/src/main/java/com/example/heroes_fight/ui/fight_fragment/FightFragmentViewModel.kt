package com.example.heroes_fight.ui.fight_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetHeroByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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


    fun getRandomHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(FightFragmentUiState.Loading)
            val baseResponseForHero = getHeroByIdUseCase(Random.nextInt(1, 732))
            val baseResponseForVillain = getHeroByIdUseCase(Random.nextInt(1, 732))

            if (baseResponseForHero is BaseResponse.Success && baseResponseForVillain is BaseResponse.Success) {
                _uiState.emit(
                    FightFragmentUiState.Success(
                        baseResponseForHero.data,
                        baseResponseForVillain.data
                    )
                )
            } else {
                _uiState.emit(FightFragmentUiState.Error(ErrorModel()))
            }
        }
    }
}