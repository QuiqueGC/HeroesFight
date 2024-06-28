package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.domain.repository.remote.response.BaseApiCallService
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.AppearanceResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.BiographyResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroesListResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.ImgResponse
import javax.inject.Inject

class ApiCallService @Inject constructor(private val remoteApiService: RemoteApiService) :
    BaseApiCallService() {

    suspend fun searchHeroByName(nameToFind: String): BaseResponse<HeroesListResponse> {
        return apiCall { remoteApiService.searchHeroByName(nameToFind) }
    }

    suspend fun getHeroById(idHero: Int): BaseResponse<HeroResponse> {
        return apiCall { remoteApiService.getHeroById(idHero) }
    }


    suspend fun getHeroBiographyById(idHero: Int): BaseResponse<BiographyResponse> {
        return apiCall { remoteApiService.getHeroBiographyById(idHero) }
    }

    suspend fun getHeroAppearanceById(idHero: Int): BaseResponse<AppearanceResponse> {
        return apiCall { remoteApiService.getHeroAppearanceById(idHero) }
    }

    suspend fun getHeroImgById(idHero: Int): BaseResponse<ImgResponse> {
        return apiCall { remoteApiService.getHeroImgById(idHero) }
    }

}