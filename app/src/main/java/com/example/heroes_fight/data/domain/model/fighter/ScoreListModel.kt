package com.example.heroes_fight.data.domain.model.fighter

import com.example.heroes_fight.data.domain.model.BaseModel

data class ScoreListModel(
    val areVillains: Boolean,
    val scores: MutableList<ScoreModel> = mutableListOf()
) : BaseModel()
