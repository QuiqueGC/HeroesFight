package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName

data class WorkResponse(
    @SerializedName("occupation") var occupation: String? = "",
    @SerializedName("base") var base: String? = ""
)