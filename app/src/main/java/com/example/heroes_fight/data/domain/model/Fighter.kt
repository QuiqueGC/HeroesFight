package com.example.heroes_fight.data.domain.model

import com.example.heroes_fight.data.domain.model.common.Position

interface Fighter {

    fun move(destinationPosition: Position): Boolean
}