package com.example.heroes_fight.data.domain.use_case.database

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import javax.inject.Inject
import kotlin.random.Random

class GetVillainListFromDBUseCase @Inject constructor(private val getFighterByIdFromDBUseCase: GetFighterByIdFromDBUseCase) {
    suspend operator fun invoke(): MutableList<FighterModel> {
        var startXPosition = 8
        val villainList = mutableListOf<FighterModel>()
        for (i in 0..4) {
            Log.i("quique", "vuelta nº $i")
            val villain = getFighterByIdFromDBUseCase(Random.nextInt(1, 732))
            villainList.add(villain)
            villainList[i].position = Position(9, startXPosition)
            villainList[i].isHero = false
            startXPosition--
        }
        return villainList
    }
}