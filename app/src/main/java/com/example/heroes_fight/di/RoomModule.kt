package com.example.heroes_fight.di

import android.content.Context
import androidx.room.Room
import com.example.heroes_fight.data.constants.MyConstants
import com.example.heroes_fight.data.domain.repository.db.HeroesDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, HeroesDB::class.java, MyConstants.HERO_DB_NAME
        ).build()

    @Singleton
    @Provides
    fun provideHeroesDao(db: HeroesDB) = db.getHeroDao()
}