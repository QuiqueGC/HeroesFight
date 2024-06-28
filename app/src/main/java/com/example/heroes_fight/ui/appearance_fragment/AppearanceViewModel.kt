package com.example.heroes_fight.ui.appearance_fragment

import androidx.lifecycle.ViewModel
import com.example.heroes_fight.data.domain.use_case.retrofit.GetAppearanceByIdUseCase
import com.example.heroes_fight.data.domain.use_case.retrofit.GetHeroImgByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppearanceViewModel @Inject constructor(
    private val getAppearanceByIdUseCase: GetAppearanceByIdUseCase,
    private val getHeroImgByIdUseCase: GetHeroImgByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppearanceUiState>(AppearanceUiState.Loading)
    val uiState: StateFlow<AppearanceUiState> = _uiState

    /*fun getHeroData(idHero: Int) {
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
    }*/
}