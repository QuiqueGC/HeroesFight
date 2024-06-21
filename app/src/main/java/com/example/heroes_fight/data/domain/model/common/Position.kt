package com.example.heroes_fight.data.domain.model.common

import java.io.Serializable

data class Position(
    var y: Int = -1,
    var x: Int = -1
) : Serializable
