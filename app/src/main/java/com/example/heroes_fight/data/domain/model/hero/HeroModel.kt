package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel

data class HeroModel(
    val id: Int = 0,
    val serialNum: String = "",
    val name: String = "",
    val alignment: String = "",
    val image: String = "",
    var intelligence: String = "",
    var strength: String = "",
    var speed: String = "",
    var durability: String = "",
    var power: String = "",
    var combat: String = "",
    var xPosition: Int = 0,
    var yPosition: Int = 0

) : BaseModel()
