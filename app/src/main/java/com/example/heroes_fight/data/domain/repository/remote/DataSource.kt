package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

interface DataSource {
    suspend fun searchHeroByName(nameHero: String): BaseResponse<MutableList<HeroModel>>
    suspend fun getHeroById(idHero: Int): BaseResponse<HeroModel>
    suspend fun getFighterById(idHero: Int): BaseResponse<FighterModel>
    suspend fun getHeroBiographyById(idHero: Int): BaseResponse<BiographyModel>
    suspend fun getHeroImgById(idHero: Int): BaseResponse<ImgModel>
    suspend fun getAppearanceById(idHero: Int): BaseResponse<AppearanceModel>
}