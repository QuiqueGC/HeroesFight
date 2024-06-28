package com.example.heroes_fight.ui.cards_collection_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.use_case.database.FindHeroesByNameFromDB
import com.example.heroes_fight.data.domain.use_case.database.GetHeroesFromDBUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsCollectionViewModel @Inject constructor(
    private val getHeroesFromDBUseCase: GetHeroesFromDBUseCase,
    private val findHeroesByNameFromDB: FindHeroesByNameFromDB
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<CardsCollectionUiState>(CardsCollectionUiState.Loading)
    val uiState: StateFlow<CardsCollectionUiState> = _uiState

    private val cardsList = mutableListOf<HeroModel>()

    fun getCardsList() {

        viewModelScope.launch {
            _uiState.emit(CardsCollectionUiState.Loading)
            cardsList.clear()
            cardsList.addAll(getHeroesFromDBUseCase())
            _uiState.emit(CardsCollectionUiState.Success(cardsList))
        }
    }

    fun getHeroFromList(position: Int): HeroModel = cardsList[position]
    fun findHero(nameHero: String) {
        viewModelScope.launch {
            _uiState.emit(CardsCollectionUiState.Loading)
            cardsList.clear()
            cardsList.addAll(findHeroesByNameFromDB(nameHero))
            Log.i("list", "cantidad de elementos en la lista -> ${cardsList.count()}")
            _uiState.emit(CardsCollectionUiState.Success(cardsList))
        }
    }
}