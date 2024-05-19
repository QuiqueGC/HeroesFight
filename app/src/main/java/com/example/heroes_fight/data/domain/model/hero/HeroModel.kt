package com.example.heroes_fight.data.domain.model.hero

import android.util.Log
import com.example.heroes_fight.data.domain.model.BaseModel
import com.example.heroes_fight.data.domain.model.Fighter
import com.example.heroes_fight.data.domain.model.common.Position
import kotlin.random.Random

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
        Log.i("quique", "NOMBRE DEL FIGHTER ---> ${name}")
        var canMove = false
        val originValue = position.y + position.x
        val destinationValue = destinationPosition.y + destinationPosition.x
        val movementCapacity = if (speed / 10 < 1) {
            1
        } else {
            speed / 10
        }

        Log.i("quique", "Posición destino (suma)---> ${destinationValue}")
        Log.i("quique", "Posición origen (suma)---> ${originValue}")
        Log.i("quique", "Speed ---> ${speed}")
        Log.i("quique", "Movement capacity  ---> ${movementCapacity}")
        Log.i("quique", "Posición en la que está actualmente  ---> ${position.y}, ${position.x}")
        Log.i(
            "quique",
            "Posición a la que se quiere mover  ---> ${destinationPosition.y}, ${destinationPosition.x}"
        )


        if (originValue < destinationValue) {
            if (originValue + movementCapacity >= destinationValue) {
                canMove = true
                position = Position(destinationPosition.y, destinationPosition.x)
                Log.i(
                    "quique",
                    "Movido a la nueva posición ---> ${destinationPosition.y}, ${destinationPosition.x}"
                )
            }

        } else {

            if (originValue - movementCapacity <= destinationValue) {
                canMove = true
                position = Position(destinationPosition.y, destinationPosition.x)
                Log.i(
                    "quique",
                    "Movido a la nueva posición ---> ${destinationPosition.y}, ${destinationPosition.x}"
                )
            }
        }
        return canMove
    }


    override fun attack(enemy: HeroModel): String {
        val heroPositionValue = position.y + position.x
        val enemyPositionValue = enemy.position.y + enemy.position.x
        val result: String

        if (heroPositionValue < enemyPositionValue) {
            result = if (enemyPositionValue - heroPositionValue == 1) {
                resolveAttack(enemy)

            } else {
                "${enemy.name} is so far"
            }

        } else {

            result = if (heroPositionValue - enemyPositionValue == 1) {

                resolveAttack(enemy)

            } else {
                "${enemy.name} is so far"
            }
        }
        return result
    }

    private fun resolveAttack(enemy: HeroModel): String {
        val attackRoll = Random.nextInt(1, 101)
        val result: String

        if (attackRoll <= combat) {
            val attackDifference = combat - attackRoll
            val enemyDefenceDifference = enemy.defenceRoll()
            if (attackDifference >= enemyDefenceDifference) {

                result = resolveDamage(attackDifference - enemyDefenceDifference, enemy)

            } else {
                result = "${enemy.name} defended the attack"
            }
        } else {
            result = "$name failed the attack"
        }

        return result
    }

    private fun resolveDamage(damageBonus: Int, enemy: HeroModel): String {
        val damageRoll = Random.nextInt(1, 101)
        val result: String
        if (damageRoll <= strength) {
            val damage = strength - damageRoll + damageBonus
            enemy.durability -= damage
            result = "$name inflicted $damage of damage to ${enemy.name}"

        } else {
            result = "$name didn't used strength enough"
        }
        return result
    }


    override fun defenceRoll(): Int {
        val defenceRoll = Random.nextInt(1, 101)
        return if (defenceRoll < combat) {
            combat - defenceRoll
        } else {
            0
        }
    }
}
