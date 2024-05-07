package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.domain.repository.remote.response.BaseApiCallService
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

class ApiCallService(private val remoteApiService: RemoteApiService) : BaseApiCallService() {

    suspend fun searchHeroByName(nameToFind: String): BaseResponse<Any> {
        return apiCall { remoteApiService.searchHeroByName(nameToFind) }
    }

    suspend fun getHeroById(idHero: Int): BaseResponse<Any> {
        return apiCall { remoteApiService.getHeroById(idHero) }
    }

    suspend fun getHeroStatsById(idHero: Int): BaseResponse<Any> {
        return apiCall { remoteApiService.getHeroStatsById(idHero) }
    }

    suspend fun getHeroBiographyById(idHero: Int): BaseResponse<Any> {
        return apiCall { remoteApiService.getHeroBiographyById(idHero) }
    }

    suspend fun getHeroAppearanceById(idHero: Int): BaseResponse<Any> {
        return apiCall { remoteApiService.getHeroAppearanceById(idHero) }
    }

    suspend fun getHeroConnectionsById(idHero: Int): BaseResponse<Any> {
        return apiCall { remoteApiService.getHeroConnectionsById(idHero) }
    }

    suspend fun getHeroImgById(idHero: Int): BaseResponse<Any> {
        return apiCall { remoteApiService.getHeroImgById(idHero) }
    }

}