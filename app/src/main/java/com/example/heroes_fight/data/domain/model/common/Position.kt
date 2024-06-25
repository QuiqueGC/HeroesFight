package com.example.heroes_fight.data.domain.model.common

import com.example.heroes_fight.data.domain.model.BaseModel
import java.io.Serializable

data class Position(
    var y: Int = -1,
    var x: Int = -1
) : BaseModel(), Serializable
