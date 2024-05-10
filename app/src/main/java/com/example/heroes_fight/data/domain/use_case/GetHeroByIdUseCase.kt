package com.example.heroes_fight.data.domain.use_case

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

class GetHeroByIdUseCase {
    suspend operator fun invoke(idHero: Int): BaseResponse<HeroModel> {
        return DataProvider.getHeroById(idHero)
    }
}