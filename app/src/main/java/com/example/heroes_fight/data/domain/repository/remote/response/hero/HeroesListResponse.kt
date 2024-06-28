package com.example.heroes_fight.data.domain.repository.remote.response.hero

import com.google.gson.annotations.SerializedName


data class HeroesListResponse(
    @SerializedName("response") var response: String? = "",
    @SerializedName("results-for") var resultsFor: String? = "",
    @SerializedName("results") var results: List<HeroResponse> = listOf()
)