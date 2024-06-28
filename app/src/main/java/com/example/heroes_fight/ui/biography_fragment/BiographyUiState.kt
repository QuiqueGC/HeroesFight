package com.example.heroes_fight.ui.biography_fragment

import com.example.heroes_fight.data.domain.model.hero.BiographyModel

sealed class BiographyUiState {
    data object Loading : BiographyUiState()
    class Success(val biographyModel: BiographyModel) : BiographyUiState()
}