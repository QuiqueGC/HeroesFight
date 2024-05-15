package com.example.heroes_fight.ui.biography_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetHeroBiographyByIdUseCase
import com.example.heroes_fight.data.domain.use_case.GetHeroImgByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiographyViewModel @Inject constructor(
    private val getHeroBiographyByIdUseCase: GetHeroBiographyByIdUseCase,
    private val getHeroImgByIdUseCase: GetHeroImgByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BiographyUiState>(BiographyUiState.Loading)
    val uiState: StateFlow<BiographyUiState> = _uiState


    fun getHeroData(idHero: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(BiographyUiState.Loading)
            val deferredImg = async { getHeroImgByIdUseCase(idHero) }
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