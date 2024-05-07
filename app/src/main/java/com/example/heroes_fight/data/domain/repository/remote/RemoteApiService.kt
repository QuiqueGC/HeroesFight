package com.example.heroes_fight.data.domain.repository.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteApiService {

    @GET("search/{nameToFind}")
    suspend fun searchHeroByName(
        @Path("nameToFind") nameToFind: String
    ): Response<Any>

    @GET("{idHero}")
    suspend fun getHeroById(
        @Path("idHero") idHero: Int
    ): Response<Any>

    @GET("{idHero}/powerstats")
    suspend fun getHeroStatsById(
        @Path("idHero") idHero: Int
    ): Response<Any>

    @GET("{idHero}/biography")
    suspend fun getHeroBiographyById(
        @Path("idHero") idHero: Int
    ): Response<Any>


    @GET("{idHero}/appearance")
    suspend fun getHeroAppearanceById(
        @Path("idHero") idHero: Int
    ): Response<Any>

    @GET("{idHero}/connections")
    suspend fun getHeroConnectionsById(
        @Path("idHero") idHero: Int
    ): Response<Any>

    @GET("{idHero}/image")
    suspend fun getHeroImgById(
        @Path("idHero") idHero: Int
    ): Response<Any>

}