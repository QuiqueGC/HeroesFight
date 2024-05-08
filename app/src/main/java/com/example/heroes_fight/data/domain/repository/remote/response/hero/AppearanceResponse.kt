package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName

data class AppearanceResponse(

    @SerializedName("response") var response: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("race") var race: String? = null,
    @SerializedName("height") var height: ArrayList<String> = arrayListOf(),
    @SerializedName("weight") var weight: ArrayList<String> = arrayListOf(),
    @SerializedName("eye-color") var eyeColor: String? = null,
    @SerializedName("hair-color") var hairColor: String? = null

)