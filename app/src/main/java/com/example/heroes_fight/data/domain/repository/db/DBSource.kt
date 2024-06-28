package com.example.heroes_fight.data.domain.repository.db

import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity
import com.example.heroes_fight.data.domain.repository.db.mapper.AppearanceEntityMapper
import com.example.heroes_fight.data.domain.repository.db.mapper.BiographyEntityMapper
import com.example.heroes_fight.data.domain.repository.db.mapper.HeroEntityMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DBSource @Inject constructor(private val heroesDao: HeroesDao) : IDBSource {
    override suspend fun getHeroesFromDB(): List<HeroModel> {
        val heroesEntities = heroesDao.getAllHeroes()
        val heroesModels = mutableListOf<HeroModel>()
        heroesEntities.forEach { heroesModels.add(HeroEntityMapper().fromEntity(it)) }
        return heroesModels
    }

    override suspend fun findHeroesByNameFromDB(heroName: String): List<HeroModel> {
        val heroesEntities = heroesDao.findHeroByName(heroName)
        val heroesModels = mutableListOf<HeroModel>()
        heroesEntities.forEach { heroesModels.add(HeroEntityMapper().fromEntity(it)) }
        return heroesModels
    }

    override suspend fun getAppearanceByIdFromDB(idHero: Int): AppearanceModel {
        return AppearanceEntityMapper().fromEntity(
            heroesDao.getHeroById(idHero).first()
        )
    }

    override suspend fun getBiographyByIdFromDB(idHero: Int): BiographyModel {
        return BiographyEntityMapper().fromEntity(
            heroesDao.getHeroById(idHero).first()
        )
    }


    override suspend fun insertHeroesAtDB(heroes: List<HeroEntity>) {
        heroesDao.insertAllHeroes(heroes)
    }
}