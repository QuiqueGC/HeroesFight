package com.example.heroes_fight.data.domain.use_case

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import javax.inject.Inject
import kotlin.random.Random

class GetHeroesListUseCase @Inject constructor(private val getFighterByIdUseCase: GetFighterByIdUseCase) {

    suspend operator fun invoke(): ArrayList<FighterModel> {
        val heroesList = ArrayList<FighterModel>()
        for (i in 0..4) {
            Log.i("quique", "vuelta nº $i")
            val baseResponseForHero = getFighterByIdUseCase(Random.nextInt(1, 732))
            if (baseResponseForHero is BaseResponse.Success)
                heroesList.add(baseResponseForHero.data)
            heroesList[i].position = Position(0, i)
            heroesList[i].isHero = true
        }
        return heroesList
    }
}