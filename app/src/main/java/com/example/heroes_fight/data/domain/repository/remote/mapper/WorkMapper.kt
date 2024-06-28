package com.example.heroes_fight.data.domain.repository.remote.mapper

import com.example.heroes_fight.data.domain.repository.db.entity.WorkEntity
import com.example.heroes_fight.data.domain.repository.remote.response.hero.WorkResponse

class WorkMapper : ResponseMapper<WorkResponse, WorkEntity> {
    override fun fromResponse(response: WorkResponse): WorkEntity {
        return WorkEntity(
            response.occupation ?: "",
            response.base ?: ""
        )
    }
}