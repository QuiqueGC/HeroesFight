package com.example.heroes_fight.data.domain.model.hero

import com.example.heroes_fight.data.domain.model.BaseModel
import com.example.heroes_fight.data.domain.model.Fighter
import com.example.heroes_fight.data.domain.model.common.Position

data class HeroModel(
    val id: Int = 0,
    val serialNum: String = "",
    val name: String = "",
    val alignment: String = "",
    val image: String = "",
    var intelligence: Int = 0,
    var strength: Int = 0,
    var speed: Int = 0,
    var durability: Int = 0,
    var power: Int = 0,
    var combat: Int = 0,
    var position: Position = Position()

) : BaseModel(), Fighter {
    override fun move(destinationPosition: Position): Boolean {
        var canMove = false
        val originValue = position.y + position.x
        val destinationValue = destinationPosition.y + destinationPosition.x
        val movementCapacity = if (speed / 10 < 1) {
            1
        } else {
            speed / 10
        }

        if (originValue < destinationValue) {
            if (originValue + movementCapacity >= destinationValue) {
                canMove = true
                position = Position(destinationPosition.y, destinationPosition.x)
            }

        } else {
            //no termino de entender por qué tengo que ponerle el +1 si en el caso
            //positivo funciona perfectamente sin añadirle o quitarle (algo se me escapa...)
            if (originValue - movementCapacity <= destinationValue + 1) {
                canMove = true
                position = Position(destinationPosition.y, destinationPosition.x)
            }
        }
        return canMove
    }
}
