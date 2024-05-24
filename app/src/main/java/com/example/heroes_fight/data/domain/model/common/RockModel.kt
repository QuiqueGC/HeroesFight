package com.example.heroes_fight.data.domain.model.common

import com.example.heroes_fight.data.domain.model.BaseModel

data class RockModel(
    val position: Position = Position()
) : BaseModel()