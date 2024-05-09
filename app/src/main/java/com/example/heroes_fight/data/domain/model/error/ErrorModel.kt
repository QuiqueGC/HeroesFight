package com.example.heroes_fight.data.domain.model.error

import com.example.heroes_fight.data.domain.model.BaseModel

data class ErrorModel(
    var error: String = "Unknown",
    var errorCode: String = "",
    var message: String = "Unknown"
) : BaseModel()