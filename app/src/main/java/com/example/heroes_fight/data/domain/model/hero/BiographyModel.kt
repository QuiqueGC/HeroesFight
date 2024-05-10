package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel

data class BiographyModel(
    var id: String = "",
    var name: String = "",
    var fullName: String = "",
    var alterEgos: String = "",
    var aliases: String = "",
    var placeOfBirth: String = "",
    var firstAppearance: String = "",
    var publisher: String = "",
    var alignment: String = ""
) : BaseModel()
