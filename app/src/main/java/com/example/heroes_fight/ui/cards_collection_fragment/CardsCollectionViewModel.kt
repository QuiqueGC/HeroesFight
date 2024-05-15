package com.example.heroes_fight.ui.cards_collection_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetHeroByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsCollectionViewModel @Inject constructor(private val getHeroByIdUseCase: GetHeroByIdUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow<CardsCollectionUiState>(CardsCollectionUiState.Loading)
    val uiState: StateFlow<CardsCollectionUiState> = _uiState


    private val cardsList = mutableListOf<HeroModel>()
    private var idHero = 1
    private var pageSize = 10

    fun getCardsList() {
        viewModelScope.launch {
            _uiState.emit(CardsCollectionUiState.Loading)
            Log.i("quique", "Empieza el bucle")

            do {
                Log.i("quique", "vuelta nº ${idHero}")
                val deferred = async { getHeroById() }

                deferred.await()

            } while (idHero < pageSize)

            pageSize += pageSize

            for (hero in cardsList) {
                Log.i("quique", "nombre del héroe -> ${hero.name}")
            }
            Log.i("quique", "emite lista")
            _uiState.emit(CardsCollectionUiState.Success(cardsList))
        }
    }


    private suspend fun getHeroById() {
        val baseResponse = getHeroByIdUseCase(idHero)
        if (baseResponse is BaseResponse.Success) {
            Log.i("quique", "El baseResponse ha sido SUCCESS")
            cardsList.add(baseResponse.data)
            idHero++
        } else {
            Log.i("quique", "El baseResponse ha sido ERROR")
        }

    }
}