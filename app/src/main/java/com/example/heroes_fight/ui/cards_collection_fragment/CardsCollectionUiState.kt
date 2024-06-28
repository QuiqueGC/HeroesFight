package com.example.heroes_fight.ui.cards_collection_fragment

import com.example.heroes_fight.data.domain.model.hero.HeroModel

sealed class CardsCollectionUiState {
    data object Loading : CardsCollectionUiState()
    class Success(val cardsList: MutableList<HeroModel>) : CardsCollectionUiState()
}