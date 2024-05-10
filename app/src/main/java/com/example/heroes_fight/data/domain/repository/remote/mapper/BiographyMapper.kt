package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.repository.remote.response.hero.BiographyResponse

class BiographyMapper : ResponseMapper<BiographyResponse, BiographyModel> {
    override fun fromResponse(response: BiographyResponse): BiographyModel {
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


        return BiographyModel(
            response.id ?: "",
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