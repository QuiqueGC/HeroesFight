package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.mapper.HeroMapper
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

object RemoteDataSource : DataSource {

    private val apiCallService = ApiCallService(RetrofitClient.getApiServices())
    override suspend fun getHeroById(idHero: Int): BaseResponse<HeroModel> {
        return when (val apiResult = apiCallService.getHeroById(idHero)) {
            is BaseResponse.Success -> BaseResponse.Success(HeroMapper().fromResponse(apiResult.data))
            is BaseResponse.Error -> BaseResponse.Error(ErrorModel())
        }
    }
}