package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName


data class StatsResponse(

    @SerializedName("response") var response: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("intelligence") var intelligence: String? = null,
    @SerializedName("strength") var strength: String? = null,
    @SerializedName("speed") var speed: String? = null,
    @SerializedName("durability") var durability: String? = null,
    @SerializedName("power") var power: String? = null,
    @SerializedName("combat") var combat: String? = null

)