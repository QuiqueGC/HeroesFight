package com.example.heroes_fight.data.domain.use_case.database

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import javax.inject.Inject
import kotlin.random.Random

class GetHeroesListFromDBUseCase @Inject constructor(private val getFighterByIdFromDBUseCase: GetFighterByIdFromDBUseCase) {
    suspend operator fun invoke(): MutableList<FighterModel> {
        val fighters = mutableListOf<FighterModel>()
        for (i in 0..4) {
            Log.i("quique", "vuelta nº $i")
            val hero = getFighterByIdFromDBUseCase(Random.nextInt(1, 732))
            fighters.add(hero)
            fighters[i].position = Position(0, i)
            fighters[i].isHero = true
        }
        return fighters
    }
}