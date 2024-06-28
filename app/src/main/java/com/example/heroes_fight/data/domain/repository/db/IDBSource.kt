package com.example.heroes_fight.data.domain.repository.db

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity


interface IDBSource {

    suspend fun getHeroesFromDB(): List<HeroModel>
    suspend fun findHeroesByNameFromDB(heroName: String): List<HeroModel>
    suspend fun getAppearanceByIdFromDB(idHero: Int): AppearanceModel
    suspend fun getBiographyByIdFromDB(idHero: Int): BiographyModel
    suspend fun insertHeroesAtDB(heroes: List<HeroEntity>)


}