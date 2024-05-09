package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName

data class BiographyResponse(

    @SerializedName("response") var response: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("full-name") var fullName: String? = null,
    @SerializedName("alter-egos") var alterEgos: String? = null,
    @SerializedName("aliases") var aliases: ArrayList<String> = arrayListOf(),
    @SerializedName("place-of-birth") var placeOfBirth: String? = null,
    @SerializedName("first-appearance") var firstAppearance: String? = null,
    @SerializedName("publisher") var publisher: String? = null,
    @SerializedName("alignment") var alignment: String? = null

)