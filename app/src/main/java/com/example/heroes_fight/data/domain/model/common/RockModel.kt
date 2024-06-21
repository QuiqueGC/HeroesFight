package com.example.heroes_fight.data.domain.model.common

import com.example.heroes_fight.data.domain.model.BaseModel
import java.io.Serializable

data class RockModel(
    val position: Position = Position()
) : BaseModel(), Serializable