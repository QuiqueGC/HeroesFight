package com.example.heroes_fight.data.domain.use_case

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import javax.inject.Inject

class GetAppearanceByIdUseCase @Inject constructor(
    private val dataProvider: DataProvider
) {
    suspend operator fun invoke(idHero: Int): BaseResponse<AppearanceModel> {
        return dataProvider.getAppearanceById(idHero)
    }
}