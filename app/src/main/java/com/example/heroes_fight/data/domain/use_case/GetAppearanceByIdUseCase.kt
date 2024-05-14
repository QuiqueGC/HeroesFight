package com.example.heroes_fight.data.domain.use_case

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

class GetAppearanceByIdUseCase {
    suspend operator fun invoke(idHero: Int): BaseResponse<AppearanceModel> {
        return DataProvider.getAppearanceById(idHero)
    }
}