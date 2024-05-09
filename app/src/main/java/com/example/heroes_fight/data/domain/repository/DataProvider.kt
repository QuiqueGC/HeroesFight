package com.example.heroes_fight.data.domain.repository

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.DataSource
import com.example.heroes_fight.data.domain.repository.remote.RemoteDataSource
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

object DataProvider : DataSource {

    override suspend fun getHeroById(idHero: Int): BaseResponse<HeroModel> {
        return RemoteDataSource.getHeroById(idHero)
    }
}