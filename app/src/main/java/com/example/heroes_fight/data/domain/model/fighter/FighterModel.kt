package com.example.heroes_fight.data.domain.model.fighter

import android.util.Log
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
    var combatBonus: Int = 0,
    var defenseBonus: Int = 0,
    var actionPerformed: Boolean = false,
    var movementPerformed: Boolean = false,
    var isSabotaged: Boolean = false
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
                movementPerformed = true
            }

        } else {

            if (originValue - movementCapacity <= destinationValue) {
                canMove = true
                position = Position(destinationPosition.y, destinationPosition.x)
                movementPerformed = true
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

    override fun sabotage(enemy: FighterModel): String {
        val heroPositionValue = position.y + position.x
        val enemyPositionValue = enemy.position.y + enemy.position.x
        val result: String

        if (heroPositionValue < enemyPositionValue) {
            result = if (enemyPositionValue - heroPositionValue == 1) {
                resolveSabotage(enemy)

            } else {
                "${enemy.name} is so far"
            }

        } else {

            result = if (heroPositionValue - enemyPositionValue == 1) {

                resolveSabotage(enemy)

            } else {
                "${enemy.name} is so far"
            }
        }
        return result
    }

    override fun defense(): String {
        defenseBonus = 20
        actionPerformed = true
        return "$name is prepared for enemy attacks"
    }

    private fun resolveSabotage(enemy: FighterModel): String {
        val sabotageRoll = Random.nextInt(1, 101)
        val result: String

        if (sabotageRoll <= intelligence) {
            val attackDifference = intelligence - sabotageRoll

            val enemyDefenceDifference = enemy.intelligenceRoll()

            if (attackDifference >= enemyDefenceDifference) {

                enemy.isSabotaged = true

                result = "${enemy.name} was sabotaged"

            } else {
                result = "${enemy.name} was more smarter"
            }
        } else {
            result = "$name failed the try of sabotage"
        }

        actionPerformed = true
        return result
    }

    private fun resolveAttack(enemy: FighterModel): String {
        val attackRoll = Random.nextInt(1, 101)
        Log.i("quique", "resultado de la tirada de ataque -> $attackRoll")
        Log.i("quique", "El valor de combat es -> $combat")
        val result: String

        if (attackRoll <= combat) {
            val attackDifference = combat - attackRoll
            val enemyDefenceDifference = enemy.defenceRoll()
            Log.i("quique", "La tiradad del enemigo de defensa es -> $enemyDefenceDifference")
            if (attackDifference >= enemyDefenceDifference) {
                var damageBonus = attackDifference - enemyDefenceDifference
                if (damageBonus > 20) {
                    damageBonus = 20
                }
                result = resolveDamage(damageBonus, enemy)

            } else {
                enemy.durability -= 5
                result = "${enemy.name} defended the attack and just received 5 of damage"
            }
        } else {
            result = "$name failed the attack"
        }

        actionPerformed = true
        return result
    }

    private fun resolveDamage(damageBonus: Int, enemy: FighterModel): String {
        val damageRoll = Random.nextInt(1, 101)
        Log.i("quique", "La tiradad de daño es -> $damageRoll")
        Log.i("quique", "La fuerza es -> $strength")
        val result: String
        if (damageRoll <= strength) {
            val damage = strength - damageRoll + damageBonus
            Log.i("quique", "El daño final es -> $damage")
            enemy.durability -= damage
            result = "$name inflicted $damage of damage to ${enemy.name}"

        } else {
            enemy.durability -= 10
            result = "$name didn't used strength enough and just caused 10 of damage"
        }
        return result
    }


    override fun defenceRoll(): Int {
        val defenceRoll = Random.nextInt(1, 101)
        Log.i("quique", "La tiradad del enemigo de defensa es -> $defenceRoll")
        Log.i("quique", "El combat del defensor es de -> $combat")
        return if (defenceRoll < combat + defenseBonus) {
            combat - defenceRoll + defenseBonus
            Log.i(
                "quique",
                "El resultado de combat - defenceRoll + defenseBonus en la siguiente línea"
            )
        } else {
            0
        }
    }

    override fun intelligenceRoll(): Int {
        val intelligenceRoll = Random.nextInt(1, 101)
        return if (intelligenceRoll < intelligence) {
            intelligence - intelligenceRoll
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

    fun refreshDataToNextTurn() {
        combatBonus = 0
        actionPerformed = false
        movementPerformed = false
        isSabotaged = false
    }

    fun removeDefenseBonus() {
        defenseBonus = 0
    }
}