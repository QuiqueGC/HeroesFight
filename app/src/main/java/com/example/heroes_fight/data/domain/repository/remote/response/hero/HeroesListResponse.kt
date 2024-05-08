package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName


data class HeroesListResponse(
    @SerializedName("response") var response: String? = null,
    @SerializedName("results-for") var resultsFor: String? = null,
    @SerializedName("results") var results: ArrayList<HeroResponse> = arrayListOf()
)