package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName

data class ImgResponse(

    @SerializedName("response") var response: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("url") var url: String? = null

)