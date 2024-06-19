package com.example.heroes_fight.data.domain.repository

import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel
import com.example.heroes_fight.data.domain.repository.remote.DataSource
import com.example.heroes_fight.data.domain.repository.remote.RemoteDataSource
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataProvider @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : DataSource {

    override suspend fun getHeroById(idHero: Int): BaseResponse<HeroModel> {
        return remoteDataSource.getHeroById(idHero)
    }

    override suspend fun searchHeroByName(nameHero: String): BaseResponse<MutableList<HeroModel>> {
        return remoteDataSource.searchHeroByName(nameHero)
    }

    override suspend fun getHeroBiographyById(idHero: Int): BaseResponse<BiographyModel> {
        return remoteDataSource.getHeroBiographyById(idHero)
    }

    override suspend fun getHeroImgById(idHero: Int): BaseResponse<ImgModel> {
        return remoteDataSource.getHeroImgById(idHero)
    }

    override suspend fun getAppearanceById(idHero: Int): BaseResponse<AppearanceModel> {
        return remoteDataSource.getAppearanceById(idHero)
    }

    override suspend fun getFighterById(idHero: Int): BaseResponse<FighterModel> {
        return remoteDataSource.getFighterById(idHero)
    }
}