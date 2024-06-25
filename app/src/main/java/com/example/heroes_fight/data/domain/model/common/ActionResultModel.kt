package com.example.heroes_fight.data.domain.model.common

import java.io.Serializable

open class ActionResultModel(
    open val txtToTvInfo: String,
    open val txtToTvActionResult: String = ""
) : Serializable
