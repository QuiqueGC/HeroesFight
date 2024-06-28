package com.example.heroes_fight.data.domain.repository.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hero_table")
data class HeroEntity(
    @PrimaryKey
    @ColumnInfo("id") var id: Int,
    @ColumnInfo("name") var name: String,
    @Embedded("powerstats") var powerstats: StatsEntity,
    @Embedded("biography") var biography: BiographyEntity,
    @Embedded("appearance") var appearance: AppearanceEntity,
    @Embedded("work") var work: WorkEntity,
    @Embedded("image") var image: ImgEntity
)
