package com.example.heroes_fight.ui.card_biography_fragment

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

class BiographyViewModel(
    private val getHeroBiographyByIdUseCase: GetHeroBiographyByIdUseCase,
    private val getHeroImgById: GetHeroImgById
) : ViewModel() {

    private val _uiState = MutableStateFlow<BiographyUiState>(BiographyUiState.Loading)
    val uiState: StateFlow<BiographyUiState> = _uiState


    fun getHeroData(idHero: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(BiographyUiState.Loading)
            val deferredImg = async { getHeroImgById(idHero) }
            val deferredBiography = async { getHeroBiographyByIdUseCase(idHero) }

            val baseResponseImgModel = deferredImg.await()
            val baseResponseBiographyModel = deferredBiography.await()

            if (baseResponseBiographyModel is BaseResponse.Success &&
                baseResponseImgModel is BaseResponse.Success
            ) {
                _uiState.emit(
                    BiographyUiState.Success(
                        baseResponseBiographyModel.data,
                        baseResponseImgModel.data
                    )
                )
            } else {
                _uiState.emit(BiographyUiState.Error(ErrorModel()))
            }
        }
    }
}