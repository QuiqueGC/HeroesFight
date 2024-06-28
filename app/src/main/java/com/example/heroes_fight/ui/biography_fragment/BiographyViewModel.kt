package com.example.heroes_fight.ui.biography_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.use_case.database.GetBiographyByIdFromDBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiographyViewModel @Inject constructor(
    private val getBiographyByIdFromDBUseCase: GetBiographyByIdFromDBUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<BiographyUiState>(BiographyUiState.Loading)
    val uiState: StateFlow<BiographyUiState> = _uiState


    fun getHeroData(idHero: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(BiographyUiState.Loading)
            val biographyModel = getBiographyByIdFromDBUseCase(idHero)
            _uiState.emit(BiographyUiState.Success(biographyModel))
        }
    }
}