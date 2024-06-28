package com.example.heroes_fight.data.domain.use_case.retrofit

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import javax.inject.Inject
import kotlin.random.Random

class GetHeroesListUseCase @Inject constructor(private val getFighterByIdUseCase: GetFighterByIdUseCase) {

    suspend operator fun invoke(): MutableList<FighterModel> {
        val fighters = mutableListOf<FighterModel>()
        for (i in 0..4) {
            Log.i("quique", "vuelta nº $i")
            val baseResponseForHero = getFighterByIdUseCase(Random.nextInt(1, 732))
            if (baseResponseForHero is BaseResponse.Success) {
                fighters.add(baseResponseForHero.data)
                fighters[i].position = Position(0, i)
                fighters[i].isHero = true
            }

        }
        return fighters
    }
}