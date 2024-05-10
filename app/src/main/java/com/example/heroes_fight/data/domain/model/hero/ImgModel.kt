package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel


data class ImgModel(
    var id: String = "",
    var name: String = "",
    var url: String = ""
) : BaseModel()
