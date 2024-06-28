package com.example.heroes_fight.data.domain.repository.remote.response.hero


import com.google.gson.annotations.SerializedName


data class HeroResponse(

    @SerializedName("response") var response: String? = "",
    @SerializedName("id") var id: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("powerstats") var powerstats: StatsResponse? = StatsResponse(),
    @SerializedName("biography") var biography: BiographyResponse? = BiographyResponse(),
    @SerializedName("appearance") var appearance: AppearanceResponse? = AppearanceResponse(),
    @SerializedName("work") var work: WorkResponse? = WorkResponse(),
    @SerializedName("connections") var connections: ConnectionsResponse? = ConnectionsResponse(),
    @SerializedName("image") var image: ImgResponse? = ImgResponse()

)