package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName
import kotlin.random.Random


data class StatsResponse(

    @SerializedName("response") var response: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("intelligence") var intelligence: String? = Random.nextInt(5, 51).toString(),
    @SerializedName("strength") var strength: String? = Random.nextInt(5, 51).toString(),
    @SerializedName("speed") var speed: String? = Random.nextInt(5, 51).toString(),
    @SerializedName("durability") var durability: String? = Random.nextInt(5, 51).toString(),
    @SerializedName("power") var power: String? = Random.nextInt(5, 51).toString(),
    @SerializedName("combat") var combat: String? = Random.nextInt(5, 51).toString()

)