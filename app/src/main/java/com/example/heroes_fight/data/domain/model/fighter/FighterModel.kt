package com.example.heroes_fight.data.domain.model.fighter

import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import kotlin.random.Random

class FighterModel(
    id: Int = 0,
    serialNum: String = "",
    name: String = "",
    alignment: String = "",
    image: String = "",
    intelligence: Int = 0,
    strength: Int = 0,
    speed: Int = 0,
    durability: Int = 0,
    power: Int = 0,
    combat: Int = 0,
    var position: Position = Position(),
    var attackBonus: Int = 0,
    var defenseBonus: Int = 0,
    var actionPerformed: Boolean = false,
    var movementPerformed: Boolean = false,
) : HeroModel(
    id,
    serialNum,
    name,
    alignment,
    image,
    intelligence,
    strength,
    speed,
    durability,
    power,
    combat,
), FighterActions {
    override fun move(destinationPosition: Position): Boolean {
        var canMove = false
        val originValue = position.y + position.x
        val destinationValue = destinationPosition.y + destinationPosition.x
        val movementCapacity = setMovementCapacity()


        if (originValue < destinationValue) {
            if (originValue + movementCapacity >= destinationValue) {
                canMove = true
                position = Position(destinationPosition.y, destinationPosition.x)
            }

        } else {

            if (originValue - movementCapacity <= destinationValue) {
                canMove = true
                position = Position(destinationPosition.y, destinationPosition.x)
            }
        }
        return canMove
    }


    override fun attack(enemy: FighterModel): String {
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

    private fun resolveAttack(enemy: FighterModel): String {
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

    private fun resolveDamage(damageBonus: Int, enemy: FighterModel): String {
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

    private fun setMovementCapacity(): Int {
        return if (speed / 10 < 1) {
            1
        } else {
            speed / 10
        }
    }
}