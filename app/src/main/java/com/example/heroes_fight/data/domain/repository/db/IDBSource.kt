package com.example.heroes_fight.data.domain.repository.db

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity


interface IDBSource {

    suspend fun getHeroesFromDB(): List<HeroModel>
    suspend fun getHeroesByNameFromDB(heroName: String): List<HeroModel>
    suspend fun insertHeroAtDB(hero: HeroEntity)


}