package com.example.heroes_fight.ui.card_biography_fragment

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel

sealed class BiographyUiState {
    data object Loading : BiographyUiState()
    class Success(val biographyModel: BiographyModel, val imgModel: ImgModel) :
        BiographyUiState()

    class Error(val errorModel: ErrorModel) : BiographyUiState()
}