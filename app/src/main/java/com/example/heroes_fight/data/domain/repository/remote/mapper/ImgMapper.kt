package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.model.hero.ImgModel
import com.example.heroes_fight.data.domain.repository.remote.response.hero.ImgResponse

class ImgMapper : ResponseMapper<ImgResponse, ImgModel> {

    override fun fromResponse(response: ImgResponse): ImgModel {
        return ImgModel(
            response.id ?: "",
            response.name ?: "",
            response.url ?: "",
        )
    }
}