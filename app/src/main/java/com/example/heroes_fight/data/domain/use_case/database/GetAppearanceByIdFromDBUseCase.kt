package com.example.heroes_fight.data.domain.use_case.database

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import javax.inject.Inject

class GetAppearanceByIdFromDBUseCase @Inject constructor(
    private val dataProvider: DataProvider
) {
    suspend operator fun invoke(idHero: Int): AppearanceModel {
        return dataProvider.getAppearanceByIdFromDB(idHero)
    }
}