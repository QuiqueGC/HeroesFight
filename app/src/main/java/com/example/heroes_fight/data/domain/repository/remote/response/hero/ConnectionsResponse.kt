package com.example.heroes_fight.data.domain.repository.remote.response.hero


import com.google.gson.annotations.SerializedName


data class ConnectionsResponse(
    @SerializedName("response") var response: String? = "",
    @SerializedName("id") var id: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("group-affiliation") var groupAffiliation: String? = "",
    @SerializedName("relatives") var relatives: String? = ""
)