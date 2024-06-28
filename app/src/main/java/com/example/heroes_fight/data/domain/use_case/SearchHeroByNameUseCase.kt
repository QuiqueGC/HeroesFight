package com.example.heroes_fight.data.domain.use_case

import com.example.heroes_fight.data.domain.repository.DataProvider
import javax.inject.Inject

class SearchHeroByNameUseCase @Inject constructor(private val dataProvider: DataProvider) {
    /*suspend operator fun invoke(nameHero: String): BaseResponse<MutableList<HeroModel>> {
        return dataProvider.searchHeroByName(nameHero)
    }*/
}