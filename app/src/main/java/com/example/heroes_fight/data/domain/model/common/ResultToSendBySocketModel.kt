package com.example.heroes_fight.data.domain.model.common

import java.io.Serializable

data class ResultToSendBySocketModel(
    override var txtToTvInfo: String = "",
    override var txtToTvActionResult: String = "",
    var action: String = "",
) : ActionResultModel(txtToTvInfo, txtToTvActionResult), Serializable
