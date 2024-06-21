package com.example.heroes_fight.data.domain.model.common

import java.io.Serializable

data class ActionResultModel(
    val txtToTvInfo: String,
    val txtToTvActionResult: String = ""
) : Serializable
