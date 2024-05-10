package com.example.heroes_fight.ui.main_menu

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel

sealed class MainMenuUiState {
    data object Loading : MainMenuUiState()

    class Success(val heroModel: HeroModel) : MainMenuUiState()

    class Error(val errorModel: ErrorModel) : MainMenuUiState()
}