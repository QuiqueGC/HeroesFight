package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel

data class HeroModel(
    var id: Int = 0,
    var serialNum: String = "",
    var name: String = "",
    var alignment: String = "",
    var image: String = "",
    var intelligence: String = "",
    var strength: String = "",
    var speed: String = "",
    var durability: String = "",
    var power: String = "",
    var combat: String = ""

) : BaseModel()
