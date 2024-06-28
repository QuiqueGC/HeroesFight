package com.example.heroes_fight.data.domain.repository.db

import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBSource @Inject constructor(heroesDao: HeroesDao) : IDBSource {
    override suspend fun getHeroesFromDB(): List<HeroModel> {

        TODO("Not yet implemented")
    }

    override suspend fun getHeroesByNameFromDB(heroName: String): List<HeroModel> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHeroAtDB(hero: HeroEntity) {
        TODO("Not yet implemented")
    }

}