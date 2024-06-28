package com.example.heroes_fight.ui.starting_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.constants.MyConstants
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.database.GetHeroesFromDBUseCase
import com.example.heroes_fight.data.domain.use_case.database.InsertHeroesAtDBUseCase
import com.example.heroes_fight.data.domain.use_case.retrofit.GetHeroByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartingFragmentViewModel @Inject constructor(
    private val getHeroByIdUseCase: GetHeroByIdUseCase,
    private val getHeroesFromDBUseCase: GetHeroesFromDBUseCase,
    private val insertHeroesAtDBUseCase: InsertHeroesAtDBUseCase
) : ViewModel() {

    private val _finnishLoading = MutableStateFlow(false)
    val finnishLoading: StateFlow<Boolean> = _finnishLoading

    private val cardsList = mutableListOf<HeroEntity>()

    private var idHero = 0
    private var offset = 1
    private var limit = MyConstants.MAX_HEROES_IN_API

    fun getCardsList() {
        Log.i("quique", "HA ENTRADO EN GET_CARDS_LIST")

            viewModelScope.launch {

                val heroes = getHeroesFromDBUseCase()
                if (heroes.isEmpty()) {
                    getHeroesFromApi()
                    val deferreds = ArrayList<Deferred<Unit>>()
                    Log.i("quique", "Empieza el bucle")
                    do {

                        val deferred = async { addHeroToList() }
                        deferreds.add(deferred)

                        offset++
                    } while (offset <= limit)

                    deferreds.awaitAll()
                    cardsList.sortBy { it.id }

                    insertHeroesAtDBUseCase(cardsList)

                    _finnishLoading.emit(true)
                } else {
                    _finnishLoading.emit(true)
                }
            }
    }

    private fun getHeroesFromApi() {

    }

    private suspend fun addHeroToList() {
        idHero++

        val baseResponse = getHeroByIdUseCase(idHero)
        if (baseResponse is BaseResponse.Success) {

            Log.i("quique", "El baseResponse ha sido SUCCESS")
            cardsList.add(baseResponse.data)
        } else {
            Log.i("quique", "El baseResponse ha sido ERROR")
        }
    }
}