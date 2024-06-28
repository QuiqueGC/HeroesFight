package com.example.heroes_fight.data.domain.use_case.database

import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import javax.inject.Inject

class GetBiographyByIdFromDBUseCase @Inject constructor(
    private val dataProvider: DataProvider
) {
    suspend operator fun invoke(idHero: Int): BiographyModel {
        return dataProvider.getBiographyByIdFromDB(idHero)
    }
}