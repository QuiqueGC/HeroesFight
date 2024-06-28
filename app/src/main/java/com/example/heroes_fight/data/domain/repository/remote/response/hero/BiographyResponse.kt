package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName

data class BiographyResponse(

    @SerializedName("response") var response: String? = "",
    @SerializedName("id") var id: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("full-name") var fullName: String? = "",
    @SerializedName("alter-egos") var alterEgos: String? = "",
    @SerializedName("aliases") var aliases: ArrayList<String> = arrayListOf(),
    @SerializedName("place-of-birth") var placeOfBirth: String? = "",
    @SerializedName("first-appearance") var firstAppearance: String? = "",
    @SerializedName("publisher") var publisher: String? = "",
    @SerializedName("alignment") var alignment: String? = ""

)