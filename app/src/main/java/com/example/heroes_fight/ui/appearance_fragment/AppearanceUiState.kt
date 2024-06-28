package com.example.heroes_fight.ui.appearance_fragment

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel

sealed class AppearanceUiState {
    data object Loading : AppearanceUiState()
    class Success(val appearanceModel: AppearanceModel) : AppearanceUiState()

}