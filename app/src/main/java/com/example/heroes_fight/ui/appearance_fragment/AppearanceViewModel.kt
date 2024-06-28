package com.example.heroes_fight.ui.appearance_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.use_case.database.GetAppearanceByIdFromDBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val getAppearanceByIdFromDBUseCase: GetAppearanceByIdFromDBUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppearanceUiState>(AppearanceUiState.Loading)
    val uiState: StateFlow<AppearanceUiState> = _uiState

    fun getHeroData(idHero: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(AppearanceUiState.Loading)
            val appearanceModel = getAppearanceByIdFromDBUseCase(idHero)
            _uiState.emit(
                AppearanceUiState.Success(appearanceModel)
            )
        }
    }
}
