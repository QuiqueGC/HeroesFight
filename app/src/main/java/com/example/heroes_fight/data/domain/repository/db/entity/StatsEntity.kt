package com.example.heroes_fight.data.domain.repository.db.entity


data class StatsEntity(
    val name: String,
    val intelligence: Int,
    val strength: Int,
    val speed: Int,
    val durability: Int,
    val power: Int,
    val combat: Int
)