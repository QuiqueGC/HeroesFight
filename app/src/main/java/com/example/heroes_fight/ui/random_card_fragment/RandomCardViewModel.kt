package com.example.heroes_fight.ui.random_card_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RandomCardViewModel @Inject constructor(
    private val getHeroByIdUseCase: GetHeroByIdUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<RandomCardUiState>(RandomCardUiState.Loading)
    val uiState: StateFlow<RandomCardUiState> = _uiState

    fun getHeroById() {

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(RandomCardUiState.Loading)

            when (val baseResponse = getHeroByIdUseCase(Random.nextInt(1, 732))) {

                is BaseResponse.Success -> {
                    _uiState.emit(RandomCardUiState.Success(baseResponse.data))
                }

                is BaseResponse.Error -> {
                    _uiState.emit(RandomCardUiState.Error(baseResponse.error))
                }
            }
        }
    }
}