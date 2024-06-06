package com.example.heroes_fight.ui.cards_collection_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.constants.MyConstants
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


    private val cardsList = mutableListOf<HeroModel>()
    private var offset = 1
    private var idHero = 0
    private var limit = 31
    private var collectionComplete = false

    fun getCardsList() {
        if (!collectionComplete) {
            val deferreds = ArrayList<Deferred<Unit>>()
            viewModelScope.launch {
                _uiState.emit(CardsCollectionUiState.Loading)
                Log.i("quique", "Empieza el bucle")

                do {
                    Log.i("quique", "vuelta nº ${offset}")
                    val deferred = async { getHeroToList() }
                    deferreds.add(deferred)
                    Log.i("quique", "${offset}")
                    offset++
                } while (offset <= limit)

                deferreds.awaitAll()
                cardsList.sortBy { it.id }

                checkIfTheListIsComplete()

                for (hero in cardsList) {
                    Log.i("quique", "nombre del héroe -> ${hero.name}")
                }
                Log.i("quique", "emite lista")
                _uiState.emit(CardsCollectionUiState.Success(cardsList))
            }
        }
    }

    private fun checkIfTheListIsComplete() {
        if (limit >= MyConstants.MAX_HEROES_IN_API) {
            collectionComplete = true
        }

        limit += limit

        if (limit > MyConstants.MAX_HEROES_IN_API) {
            limit = MyConstants.MAX_HEROES_IN_API
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

    fun getHeroFromList(position: Int): HeroModel = cardsList[position]
}