package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel

open class HeroModel(
    open val id: Int = 0,
    val serialNum: String = "",
    val name: String = "",
    val alignment: String = "good",
    val image: String = "",
    var intelligence: Int = 0,
    var strength: Int = 0,
    var speed: Int = 0,
    var durability: Int = 0,
    var power: Int = 0,
    var combat: Int = 0
    ) : BaseModel()
