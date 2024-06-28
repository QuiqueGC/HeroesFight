package com.example.heroes_fight.data.domain.repository.remote

import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

interface DataSource {
    suspend fun searchHeroByName(nameHero: String): BaseResponse<MutableList<HeroEntity>>
    suspend fun getHeroById(idHero: Int): BaseResponse<HeroEntity>
    suspend fun getFighterById(idHero: Int): BaseResponse<FighterModel>
    //suspend fun getHeroBiographyById(idHero: Int): BaseResponse<BiographyModel>
    //suspend fun getHeroImgById(idHero: Int): BaseResponse<ImgModel>
    //suspend fun getAppearanceById(idHero: Int): BaseResponse<AppearanceModel>
}