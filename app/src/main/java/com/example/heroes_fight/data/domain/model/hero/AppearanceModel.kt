package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel

data class AppearanceModel(
    var id: String = "",
    var name: String = "",
    var gender: String = "",
    var race: String = "",
    var heightCm: String = "",
    var heightFeet: String = "",
    var weightKg: String = "",
    var weightLb: String = "",
    var eyeColor: String = "",
    var hairColor: String = ""
) : BaseModel()