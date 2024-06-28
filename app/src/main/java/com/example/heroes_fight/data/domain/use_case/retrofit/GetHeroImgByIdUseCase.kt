package com.example.heroes_fight.data.domain.use_case.retrofit

import com.example.heroes_fight.data.domain.repository.DataProvider
import javax.inject.Inject

class GetHeroImgByIdUseCase @Inject constructor(
    private val dataProvider: DataProvider
) {

    /*suspend operator fun invoke(idHero: Int): BaseResponse<ImgModel> {
        return dataProvider.getHeroImgById(idHero)
    }*/
}