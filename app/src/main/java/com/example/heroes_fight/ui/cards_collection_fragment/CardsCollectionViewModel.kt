package com.example.heroes_fight.ui.cards_collection_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetHeroByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsCollectionViewModel @Inject constructor(private val getHeroByIdUseCase: GetHeroByIdUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow<CardsCollectionUiState>(CardsCollectionUiState.Loading)
    val uiState: StateFlow<CardsCollectionUiState> = _uiState

    private val _selectedHero = MutableStateFlow(HeroModel())
    val selectedHero: StateFlow<HeroModel> = _selectedHero

    private val cardsList = mutableListOf<HeroModel>()
    private var loops = 1
    private var idHero = 0
    private var pageSize = 10

    fun getCardsList() {
        val deferreds = ArrayList<Deferred<Unit>>()
        viewModelScope.launch {
            _uiState.emit(CardsCollectionUiState.Loading)
            Log.i("quique", "Empieza el bucle")

            do {
                Log.i("quique", "vuelta nº ${loops}")
                val deferred = async { getHeroToList() }
                deferreds.add(deferred)
                Log.i("quique", "${loops}")
                loops++
            } while (loops < pageSize)

            deferreds.awaitAll()
            cardsList.sortBy { it.id }
            pageSize += pageSize

            for (hero in cardsList) {
                Log.i("quique", "nombre del héroe -> ${hero.name}")
            }
            Log.i("quique", "emite lista")
            _uiState.emit(CardsCollectionUiState.Success(cardsList))
        }
    }

    private suspend fun getHeroToList() {
        idHero++
        Log.i("quique", "El id que paso a la llamada es -> ${idHero}")
        val baseResponse = getHeroByIdUseCase(idHero)
        if (baseResponse is BaseResponse.Success) {
            Log.i("quique", "El baseResponse ha sido SUCCESS")
            cardsList.add(baseResponse.data)
        } else {
            Log.i("quique", "El baseResponse ha sido ERROR")
        }
    }

    fun getHeroFromList(position: Int) {
        viewModelScope.launch {
            _selectedHero.emit(cardsList[position])
        }
    }
}