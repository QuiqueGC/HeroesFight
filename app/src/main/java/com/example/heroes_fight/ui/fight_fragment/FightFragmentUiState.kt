package com.example.heroes_fight.ui.fight_fragment

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel

sealed class FightFragmentUiState {

    data object Loading : FightFragmentUiState()
    class Success(
        val heroesList: ArrayList<FighterModel>,
        val villainsList: ArrayList<FighterModel>
    ) :
        FightFragmentUiState()
    class Error(val errorModel: ErrorModel) : FightFragmentUiState()
}