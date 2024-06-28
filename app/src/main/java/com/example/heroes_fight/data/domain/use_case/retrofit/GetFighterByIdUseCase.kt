package com.example.heroes_fight.data.domain.use_case.retrofit

import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.remote.response.BaseResponse
import javax.inject.Inject

class GetFighterByIdUseCase @Inject constructor(private val dataProvider: DataProvider) {
    suspend operator fun invoke(idHero: Int): BaseResponse<FighterModel> {
        return dataProvider.getFighterById(idHero)
    }
}