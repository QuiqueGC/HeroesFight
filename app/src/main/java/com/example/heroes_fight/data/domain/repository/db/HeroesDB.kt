package com.example.heroes_fight.data.domain.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.heroes_fight.data.domain.repository.db.entity.HeroEntity

@Database(entities = [HeroEntity::class], version = 1)
abstract class HeroesDB : RoomDatabase() {
    abstract fun getHeroDao(): HeroesDao
}