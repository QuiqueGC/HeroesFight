package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel
import com.example.heroes_fight.data.domain.repository.remote.mapper.AppearanceMapper
import com.example.heroes_fight.data.domain.repository.remote.mapper.BiographyMapper
import com.example.heroes_fight.data.domain.repository.remote.mapper.FighterMapper
import com.example.heroes_fight.data.domain.repository.remote.mapper.HeroMapper
import com.example.heroes_fight.data.domain.repository.remote.mapper.HeroesListMapper
import com.example.heroes_fight.data.domain.repository.remote.mapper.ImgMapper
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiCallService: ApiCallService) :
    DataSource {

    override suspend fun getHeroById(idHero: Int): BaseResponse<HeroModel> {
        return when (val apiResult = apiCallService.getHeroById(idHero)) {
            is BaseResponse.Success -> BaseResponse.Success(HeroMapper().fromResponse(apiResult.data))
            is BaseResponse.Error -> BaseResponse.Error(ErrorModel())
        }
    }

    override suspend fun searchHeroByName(nameHero: String): BaseResponse<MutableList<HeroModel>> {
        return when (val apiResult = apiCallService.searchHeroByName(nameHero)) {
            is BaseResponse.Success -> BaseResponse.Success(
                HeroesListMapper().fromResponse(
                    apiResult.data
                )
            )

            is BaseResponse.Error -> BaseResponse.Error(ErrorModel())
        }
    }

    override suspend fun getHeroBiographyById(idHero: Int): BaseResponse<BiographyModel> {
        return when (val apiResult = apiCallService.getHeroBiographyById(idHero)) {
            is BaseResponse.Success -> BaseResponse.Success(BiographyMapper().fromResponse(apiResult.data))
            is BaseResponse.Error -> BaseResponse.Error(ErrorModel())
        }
    }

    override suspend fun getHeroImgById(idHero: Int): BaseResponse<ImgModel> {
        return when (val apiResult = apiCallService.getHeroImgById(idHero)) {
            is BaseResponse.Success -> BaseResponse.Success(ImgMapper().fromResponse(apiResult.data))
            is BaseResponse.Error -> BaseResponse.Error(ErrorModel())
        }
    }

    override suspend fun getAppearanceById(idHero: Int): BaseResponse<AppearanceModel> {
        return when (val apiResult = apiCallService.getHeroAppearanceById(idHero)) {
            is BaseResponse.Success -> BaseResponse.Success(
                AppearanceMapper().fromResponse(
                    apiResult.data
                )
            )

            is BaseResponse.Error -> BaseResponse.Error(ErrorModel())
        }
    }

    override suspend fun getFighterById(idHero: Int): BaseResponse<FighterModel> {
        return when (val apiResult = apiCallService.getHeroById(idHero)) {
            is BaseResponse.Success -> BaseResponse.Success(FighterMapper().fromResponse(apiResult.data))
            is BaseResponse.Error -> BaseResponse.Error(ErrorModel())
        }
    }
}