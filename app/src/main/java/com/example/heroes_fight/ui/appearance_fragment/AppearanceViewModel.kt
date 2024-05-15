package com.example.heroes_fight.ui.appearance_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.use_case.GetAppearanceByIdUseCase
import com.example.heroes_fight.data.domain.use_case.GetHeroImgByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppearanceViewModel @Inject constructor(
    private val getAppearanceByIdUseCase: GetAppearanceByIdUseCase,
    private val getHeroImgByIdUseCase: GetHeroImgByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppearanceUiState>(AppearanceUiState.Loading)
    val uiState: StateFlow<AppearanceUiState> = _uiState

    fun getHeroData(idHero: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(AppearanceUiState.Loading)
            val deferredImg = async { getHeroImgByIdUseCase(idHero) }
            val deferredAppearance = async { getAppearanceByIdUseCase(idHero) }

            val baseResponseImgModel = deferredImg.await()
            val baseResponseAppearanceModel = deferredAppearance.await()

            if (baseResponseAppearanceModel is BaseResponse.Success &&
                baseResponseImgModel is BaseResponse.Success
            ) {
                _uiState.emit(
                    AppearanceUiState.Success(
                        baseResponseAppearanceModel.data,
                        baseResponseImgModel.data
                    )
                )
            } else {
                _uiState.emit(AppearanceUiState.Error(ErrorModel()))
            }
        }
    }
}