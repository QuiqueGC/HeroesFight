package com.example.heroes_fight.ui.random_card_fragment

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel

sealed class RandomCardUiState {
    data object Loading : RandomCardUiState()

    class Success(val heroModel: HeroModel) : RandomCardUiState()

    class Error(val errorModel: ErrorModel) : RandomCardUiState()
}