package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel

data class BiographyModel(
    val name: String = "",
    val fullName: String = "",
    val alterEgos: String = "",
    val aliases: String = "",
    val placeOfBirth: String = "",
    val firstAppearance: String = "",
    val publisher: String = "",
    val alignment: String = "",
    val image: String = "",
) : BaseModel()
