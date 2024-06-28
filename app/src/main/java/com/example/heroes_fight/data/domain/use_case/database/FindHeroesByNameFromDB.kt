package com.example.heroes_fight.data.domain.use_case.database

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import javax.inject.Inject

class FindHeroesByNameFromDB @Inject constructor(
    private val dataProvider: DataProvider
) {
    suspend operator fun invoke(nameHero: String): List<HeroModel> {
        return dataProvider.findHeroesByNameFromDB(nameHero)
    }
}