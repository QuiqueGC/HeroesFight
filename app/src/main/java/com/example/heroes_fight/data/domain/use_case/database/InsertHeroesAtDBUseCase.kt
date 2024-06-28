package com.example.heroes_fight.data.domain.use_case.database

import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import javax.inject.Inject

class InsertHeroesAtDBUseCase @Inject constructor(
    private val dataProvider: DataProvider
) {
    suspend operator fun invoke(heroes: List<HeroEntity>) {
        return dataProvider.insertHeroesAtDB(heroes)
    }
}