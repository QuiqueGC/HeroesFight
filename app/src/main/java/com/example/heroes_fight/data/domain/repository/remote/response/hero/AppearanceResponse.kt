package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName

data class AppearanceResponse(

    @SerializedName("response") var response: String? = "",
    @SerializedName("id") var id: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("gender") var gender: String? = "",
    @SerializedName("race") var race: String? = "",
    @SerializedName("height") var height: ArrayList<String> = arrayListOf(),
    @SerializedName("weight") var weight: ArrayList<String> = arrayListOf(),
    @SerializedName("eye-color") var eyeColor: String? = "",
    @SerializedName("hair-color") var hairColor: String? = ""

)