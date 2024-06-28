package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.repository.db.entity.BiographyEntity
import com.example.heroes_fight.data.domain.repository.remote.response.hero.BiographyResponse

class BiographyMapper : ResponseMapper<BiographyResponse, BiographyEntity> {
    override fun fromResponse(response: BiographyResponse): BiographyEntity {
        var aliases = ""
        if (response.aliases.isNotEmpty()) {
            response.aliases.forEach {
                aliases = if (aliases == "") {
                    it
                } else {
                    "$aliases, $it"
                }
            }
        }

        return BiographyEntity(
            response.name ?: "",
            response.fullName ?: "",
            response.alterEgos ?: "",
            aliases,
            response.placeOfBirth ?: "",
            response.firstAppearance ?: "",
            response.publisher ?: "",
            response.alignment ?: "",
        )
    }
}