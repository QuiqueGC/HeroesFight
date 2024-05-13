package com.example.heroes_fight.ui.card_detail_fragment

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel

sealed class BiographyDetailUiState {
    data object Loading : BiographyDetailUiState()
    class Success(val biographyModel: BiographyModel, val imgModel: ImgModel) :
        BiographyDetailUiState()

    class Error(val errorModel: ErrorModel) : BiographyDetailUiState()
}