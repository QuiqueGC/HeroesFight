package com.example.heroes_fight.data.domain.use_case

import com.example.heroes_fight.data.domain.model.hero.ImgModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse

class GetHeroImgById {

    suspend operator fun invoke(idHero: Int): BaseResponse<ImgModel> {
        return DataProvider.getHeroImgById(idHero)
    }
}