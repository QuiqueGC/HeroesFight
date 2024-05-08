package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.domain.repository.remote.response.hero.AppearanceResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.BiographyResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.ConnectionsResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.HeroesListResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.ImgResponse
import com.example.heroes_fight.data.domain.repository.remote.response.hero.StatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteApiService {

    @GET("search/{nameToFind}")
    suspend fun searchHeroByName(
        @Path("nameToFind") nameToFind: String
    ): Response<HeroesListResponse>

    @GET("{idHero}")
    suspend fun getHeroById(
        @Path("idHero") idHero: Int
    ): Response<HeroResponse>

    @GET("{idHero}/powerstats")
    suspend fun getHeroStatsById(
        @Path("idHero") idHero: Int
    ): Response<StatsResponse>

    @GET("{idHero}/biography")
    suspend fun getHeroBiographyById(
        @Path("idHero") idHero: Int
    ): Response<BiographyResponse>


    @GET("{idHero}/appearance")
    suspend fun getHeroAppearanceById(
        @Path("idHero") idHero: Int
    ): Response<AppearanceResponse>

    @GET("{idHero}/connections")
    suspend fun getHeroConnectionsById(
        @Path("idHero") idHero: Int
    ): Response<ConnectionsResponse>

    @GET("{idHero}/image")
    suspend fun getHeroImgById(
        @Path("idHero") idHero: Int
    ): Response<ImgResponse>

}