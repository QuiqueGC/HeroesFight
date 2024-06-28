package com.example.heroes_fight.data.domain.use_case.database

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import javax.inject.Inject

class GetHeroesFromDBUseCase @Inject constructor(
    private val dataProvider: DataProvider
) {
    suspend operator fun invoke(): List<HeroModel> {
        return dataProvider.getHeroesFromDB()
    }
}