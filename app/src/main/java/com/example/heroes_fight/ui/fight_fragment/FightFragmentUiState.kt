package com.example.heroes_fight.ui.fight_fragment

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel

sealed class FightFragmentUiState {

    data object Loading : FightFragmentUiState()
    class Success(val hero: HeroModel, val villain: HeroModel) : FightFragmentUiState()
    class Error(val errorModel: ErrorModel) : FightFragmentUiState()
}