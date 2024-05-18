package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel


data class ImgModel(
    val id: String = "",
    val name: String = "",
    val url: String = ""
) : BaseModel()
