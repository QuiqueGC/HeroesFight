package com.example.heroes_fight.data.domain.repository.db.entity

data class BiographyEntity(
    val name: String,
    val fullName: String,
    val alterEgos: String,
    val aliases: String,
    val placeOfBirth: String,
    val firstAppearance: String,
    val publisher: String,
    val alignment: String
)