package com.example.heroes_fight.data.domain.use_case.database

import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import javax.inject.Inject

class GetFighterByIdFromDBUseCase @Inject constructor(private val dataProvider: DataProvider) {
    suspend operator fun invoke(idHero: Int): FighterModel {
        return dataProvider.getFighterByIdFromDB(idHero)
    }
}