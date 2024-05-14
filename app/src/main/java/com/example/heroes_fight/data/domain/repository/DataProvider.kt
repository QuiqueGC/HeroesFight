package com.example.heroes_fight.data.domain.repository

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel
import com.example.heroes_fight.data.domain.repository.remote.DataSource
import com.example.heroes_fight.data.domain.repository.remote.RemoteDataSource
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

object DataProvider : DataSource {

    override suspend fun getHeroById(idHero: Int): BaseResponse<HeroModel> {
        return RemoteDataSource.getHeroById(idHero)
    }

    override suspend fun getHeroBiographyById(idHero: Int): BaseResponse<BiographyModel> {
        return RemoteDataSource.getHeroBiographyById(idHero)
    }

    override suspend fun getHeroImgById(idHero: Int): BaseResponse<ImgModel> {
        return RemoteDataSource.getHeroImgById(idHero)
    }

    override suspend fun getAppearanceById(idHero: Int): BaseResponse<AppearanceModel> {
        return RemoteDataSource.getAppearanceById(idHero)
    }
}