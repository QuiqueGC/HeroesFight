package com.example.heroes_fight.data.domain.model.fighter

import com.example.heroes_fight.data.domain.model.BaseModel

data class ScoreModel(
    val serialNum: String,
    val name: String,
    val img: String,
    var kills: Int = 0,
    var meleeAtks: Int = 0,
    var meleeDmg: Int = 0,
    var rangeAtks: Int = 0,
    var rangeDmg: Int = 0,
    var defAtks: Int = 0,
    var meleeDmgRec: Int = 0,
    var dodgedAtks: Int = 0,
    var rangeDmgRec: Int = 0,
    var survived: Boolean = true,
) : BaseModel()
