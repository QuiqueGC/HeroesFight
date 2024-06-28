package com.example.heroes_fight.data.domain.use_case.retrofit

import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import javax.inject.Inject

class GetHeroByIdUseCase @Inject constructor(private val dataProvider: DataProvider) {
    suspend operator fun invoke(idHero: Int): BaseResponse<HeroEntity> {
        return dataProvider.getHeroById(idHero)
    }
}