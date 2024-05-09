package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

interface DataSource {
    suspend fun getHeroById(idHero: Int): BaseResponse<HeroModel>
}