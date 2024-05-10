package com.example.heroes_fight.ui.card_detail_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetHeroBiographyByIdUseCase
import com.example.heroes_fight.data.domain.use_case.GetHeroImgById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardDetailViewModel(
    private val getHeroBiographyByIdUseCase: GetHeroBiographyByIdUseCase,
    private val getHeroImgById: GetHeroImgById
) : ViewModel() {

    private val _uiState = MutableStateFlow<CardDetailUiState>(CardDetailUiState.Loading)
    val uiState: StateFlow<CardDetailUiState> = _uiState


    fun getHeroData(idHero: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(CardDetailUiState.Loading)
            val deferredImg = async { getHeroImgById(idHero) }
            val deferredBiography = async { getHeroBiographyByIdUseCase(idHero) }

            val baseResponseImgModel = deferredImg.await()
            val baseResponseBiographyModel = deferredBiography.await()

            if (baseResponseBiographyModel is BaseResponse.Success &&
                baseResponseImgModel is BaseResponse.Success
            ) {
                _uiState.emit(
                    CardDetailUiState.Success(
                        baseResponseBiographyModel.data,
                        baseResponseImgModel.data
                    )
                )
            } else {
                _uiState.emit(CardDetailUiState.Error(ErrorModel()))
            }
        }
    }
}