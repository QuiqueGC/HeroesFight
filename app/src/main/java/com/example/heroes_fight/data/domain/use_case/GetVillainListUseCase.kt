package com.example.heroes_fight.data.domain.use_case

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import javax.inject.Inject
import kotlin.random.Random

class GetVillainListUseCase @Inject constructor(private val getFighterByIdUseCase: GetFighterByIdUseCase) {

    suspend operator fun invoke(): ArrayList<FighterModel> {
        var startXPosition = 8
        val villainList = ArrayList<FighterModel>()
        for (i in 0..4) {
            Log.i("quique", "vuelta nº $i")
            val baseResponseForVillain = getFighterByIdUseCase(Random.nextInt(1, 732))
            if (baseResponseForVillain is BaseResponse.Success) {
                villainList.add(baseResponseForVillain.data)
                villainList[i].position = Position(9, startXPosition)
                villainList[i].isHero = false
                startXPosition--
            }
        }
        return villainList
    }
}