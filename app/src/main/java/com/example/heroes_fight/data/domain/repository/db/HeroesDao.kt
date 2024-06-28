package com.example.heroes_fight.data.domain.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity

@Dao
interface HeroesDao {
    @Query("SELECT * FROM hero_table")
    suspend fun getAllHeroes(): List<HeroEntity>

    @Query("SELECT * FROM hero_table where name like :heroName")
    suspend fun findByName(heroName: String): List<HeroEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHero(hero: HeroEntity)
}