package com.example.heroes_fight.ui.main_menu_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetHeroByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainMenuViewModel(private val getHeroByIdUseCase: GetHeroByIdUseCase) : ViewModel() {


    private val _uiState = MutableStateFlow<MainMenuUiState>(MainMenuUiState.Loading)
    val uiState: StateFlow<MainMenuUiState> = _uiState

    fun getHeroById() {

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(MainMenuUiState.Loading)

            val baseResponse = getHeroByIdUseCase(Random.nextInt(1, 732))

            when (baseResponse) {
                is BaseResponse.Success -> {
                    _uiState.emit(MainMenuUiState.Success(baseResponse.data))
                }

                is BaseResponse.Error -> {
                    _uiState.emit(MainMenuUiState.Error(baseResponse.error))
                }
            }
        }
    }
}