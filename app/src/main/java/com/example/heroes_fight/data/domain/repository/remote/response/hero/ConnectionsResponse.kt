package com.example.heroes_fight.data.domain.repository.remote.response.hero


import com.google.gson.annotations.SerializedName


data class ConnectionsResponse(
    @SerializedName("response") var response: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("group-affiliation") var groupAffiliation: String? = null,
    @SerializedName("relatives") var relatives: String? = null
)