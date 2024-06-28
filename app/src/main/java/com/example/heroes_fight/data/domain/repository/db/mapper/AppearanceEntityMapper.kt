package com.example.heroes_fight.data.domain.repository.db.mapper

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity

class AppearanceEntityMapper : EntityMapper<HeroEntity, AppearanceModel> {
    override fun fromEntity(entity: HeroEntity): AppearanceModel {
        return AppearanceModel(
            entity.name,
            entity.appearance.gender,
            entity.appearance.race,
            entity.appearance.heightCm,
            entity.appearance.heightFeet,
            entity.appearance.weightKg,
            entity.appearance.weightLb,
            entity.appearance.eyeColor,
            entity.appearance.hairColor,
            entity.image.url
        )
    }
}