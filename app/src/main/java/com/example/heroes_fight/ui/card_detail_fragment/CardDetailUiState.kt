package com.example.heroes_fight.ui.card_detail_fragment

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel

sealed class CardDetailUiState {
    data object Loading : CardDetailUiState()
    class Success(val biographyModel: BiographyModel, val imgModel: ImgModel) : CardDetailUiState()
    class Error(val errorModel: ErrorModel) : CardDetailUiState()
}