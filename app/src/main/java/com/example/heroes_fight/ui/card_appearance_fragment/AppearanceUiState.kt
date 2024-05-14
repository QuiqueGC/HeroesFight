package com.example.heroes_fight.ui.card_appearance_fragment

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel

sealed class AppearanceUiState {
    data object Loading : AppearanceUiState()
    class Success(val appearanceModel: AppearanceModel, val imgModel: ImgModel) :
        AppearanceUiState()

    class Error(val errorModel: ErrorModel) : AppearanceUiState()
}