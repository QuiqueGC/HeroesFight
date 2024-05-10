package com.example.heroes_fight.data.domain.use_case

import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

class GetHeroBiographyByIdUseCase {

    suspend operator fun invoke(idHero: Int): BaseResponse<BiographyModel> {
        return DataProvider.getHeroBiographyById(idHero)
    }
}