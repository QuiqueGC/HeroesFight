package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel

data class AppearanceModel(
    val name: String = "",
    val gender: String = "",
    val race: String = "",
    val heightCm: String = "",
    val heightFeet: String = "",
    val weightKg: String = "",
    val weightLb: String = "",
    val eyeColor: String = "",
    val hairColor: String = "",
    val image: String = "",
) : BaseModel()