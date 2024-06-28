package com.example.heroes_fight.data.domain.repository.db.mapper

import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity

class BiographyEntityMapper : EntityMapper<HeroEntity, BiographyModel> {
    override fun fromEntity(entity: HeroEntity): BiographyModel {
        return BiographyModel(
            entity.name,
            entity.biography.fullName,
            entity.biography.alterEgos,
            entity.biography.aliases,
            entity.biography.placeOfBirth,
            entity.biography.firstAppearance,
            entity.biography.publisher,
            entity.biography.alignment,
            entity.image.url
        )
    }
}