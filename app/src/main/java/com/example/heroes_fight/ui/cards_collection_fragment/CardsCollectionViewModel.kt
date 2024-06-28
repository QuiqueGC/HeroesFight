package com.example.heroes_fight.ui.cards_collection_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.heroes_fight.data.constants.MyConstants
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.use_case.retrofit.GetHeroByIdUseCase
import com.example.heroes_fight.data.domain.use_case.retrofit.SearchHeroByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CardsCollectionViewModel @Inject constructor(
    private val getHeroByIdUseCase: GetHeroByIdUseCase,
    private val searchHeroByNameUseCase: SearchHeroByNameUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<CardsCollectionUiState>(CardsCollectionUiState.Loading)
    val uiState: StateFlow<CardsCollectionUiState> = _uiState


    private val cardsList = mutableListOf<HeroModel>()
    private var offset = 1
    private var idHero = 0
    private var limit = 31
    private var collectionComplete = false

    /*fun getCardsList() {
        Log.i("quique", "HA ENTRADO EN GET_CARDS_LIST")
        if (!collectionComplete) {
            val deferreds = ArrayList<Deferred<Unit>>()
            viewModelScope.launch {
                _uiState.emit(CardsCollectionUiState.Loading)
                Log.i("quique", "Empieza el bucle")

                do {
                    Log.i("quique", "vuelta nº $offset")
                    val deferred = async { getHeroToList() }
                    deferreds.add(deferred)
                    Log.i("quique", "$offset")
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
    }*/

    /*fun searchHero(nameHero: String) {
        Log.i("quique", "HA ENTRADO EN SEARCH_HERO")
        viewModelScope.launch {
            _uiState.emit(CardsCollectionUiState.Loading)
            when (val baseResponse = searchHeroByNameUseCase(nameHero)) {
                is BaseResponse.Error -> _uiState.emit(CardsCollectionUiState.Error(baseResponse.error))
                is BaseResponse.Success -> {
                    cardsList.clear()
                    cardsList.addAll(baseResponse.data)
                    _uiState.emit(CardsCollectionUiState.Success(baseResponse.data))
                }
            }
        }
    }*/

    fun restartList() {
        Log.i("quique", "HA ENTRADO EN RESTART_LIST")
        offset = 1
        idHero = 0
        limit = 31
        cardsList.clear()
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

    /*private suspend fun getHeroToList() {
        idHero++
        Log.i("quique", "El id que paso a la llamada es -> $idHero")
        val baseResponse = getHeroByIdUseCase(idHero)
        if (baseResponse is BaseResponse.Success) {
            Log.i("quique", "El baseResponse ha sido SUCCESS")
            cardsList.add(baseResponse.data)
        } else {
            Log.i("quique", "El baseResponse ha sido ERROR")
        }
    }*/

    fun getHeroFromList(position: Int): HeroModel = cardsList[position]
}