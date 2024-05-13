package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.repository.remote.response.hero.AppearanceResponse

class AppearanceMapper : ResponseMapper<AppearanceResponse, AppearanceModel> {
    override fun fromResponse(response: AppearanceResponse): AppearanceModel {
        var heightCm = ""
        var heightFeet = ""
        var weightKg = ""
        var weightLb = ""

        if (response.height.isNotEmpty() && response.height.size >= 2) {
            heightCm = response.height[1]
            heightFeet = response.height[0]
        }
        if (response.weight.isNotEmpty() && response.weight.size >= 2) {
            weightKg = response.weight[1]
            weightLb = response.weight[0]
        }

        return AppearanceModel(
            response.id ?: "",
            response.name ?: "",
            response.gender ?: "",
            response.race ?: "",
            heightCm,
            heightFeet,
            weightKg,
            weightLb,
            response.eyeColor ?: "",
            response.hairColor ?: ""
        )
    }
}