package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.repository.db.entity.ImgEntity
import com.example.heroes_fight.data.domain.repository.remote.response.hero.ImgResponse

class ImgMapper : ResponseMapper<ImgResponse, ImgEntity> {

    override fun fromResponse(response: ImgResponse): ImgEntity {
        return ImgEntity(
            response.url ?: "",
        )
    }
}