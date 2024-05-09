package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel

data class HeroModel(
    var id: String = "",
    var name: String = "",
    var alignment: String = "",
    var image: String = "",
    var intelligence: Int = 0,
    var strength: Int = 0,
    var speed: Int = 0,
    var durability: Int = 0,
    var power: Int = 0,
    var combat: Int = 0

) : BaseModel()
