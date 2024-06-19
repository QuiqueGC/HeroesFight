package com.example.heroes_fight.data.domain.model.fighter

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.utils.Bresenham
import kotlin.math.abs
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
    var movementCapacity: Int = 1,
    var distanceToShot: Int = 1,
    var position: Position = Position(),
    var combatBonus: Int = 0,
    var defenseBonus: Int = 0,
    var actionPerformed: Boolean = false,
    var movementPerformed: Boolean = false,
    var isSabotaged: Boolean = false,
    var isHero: Boolean = true,
    val score: ScoreModel = ScoreModel(serialNum, name, image)
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
        var difference: Int
        var result: Int
        with(position) {
            if (x < destinationPosition.x && y < destinationPosition.y) {
                if (x + movementCapacity >= destinationPosition.x) {
                    difference = destinationPosition.x - x
                    result = movementCapacity - difference

                    if (y + result >= destinationPosition.y) {
                        canMove = true
                        position = Position(destinationPosition.y, destinationPosition.x)
                        movementPerformed = true
                    }
                }
            } else if (x > destinationPosition.x && y > destinationPosition.y) {
                if (x - movementCapacity <= destinationPosition.x) {
                    difference = x - destinationPosition.x
                    result = movementCapacity - difference
                    if (y - result <= destinationPosition.y) {
                        canMove = true
                        position = Position(destinationPosition.y, destinationPosition.x)
                        movementPerformed = true
                    }
                }
            } else if (x < destinationPosition.x && y > destinationPosition.y) {
                if (x + movementCapacity >= destinationPosition.x) {
                    difference = destinationPosition.x - x
                    result = movementCapacity - difference
                    if (y - result <= destinationPosition.y) {
                        canMove = true
                        position = Position(destinationPosition.y, destinationPosition.x)
                        movementPerformed = true
                    }
                }
            } else if (x > destinationPosition.x && y < destinationPosition.y) {
                if (x - movementCapacity <= destinationPosition.x) {
                    difference = x - destinationPosition.x
                    result = movementCapacity - difference
                    if (y + result >= destinationPosition.y) {
                        canMove = true
                        position = Position(destinationPosition.y, destinationPosition.x)
                        movementPerformed = true
                    }
                }
            } else if (x == destinationPosition.x && y < destinationPosition.y) {
                if (y + movementCapacity >= destinationPosition.y) {
                    canMove = true
                    position = Position(destinationPosition.y, destinationPosition.x)
                    movementPerformed = true
                }
            } else if (x == destinationPosition.x && y > destinationPosition.y) {
                if (y - movementCapacity <= destinationPosition.y) {
                    canMove = true
                    position = Position(destinationPosition.y, destinationPosition.x)
                    movementPerformed = true
                }
            } else if (x < destinationPosition.x && y == destinationPosition.y) {
                if (x + movementCapacity >= destinationPosition.x) {
                    canMove = true
                    position = Position(destinationPosition.y, destinationPosition.x)
                    movementPerformed = true
                }
            } else if (x > destinationPosition.x && y == destinationPosition.y) {
                if (x - movementCapacity <= destinationPosition.x) {
                    canMove = true
                    position = Position(destinationPosition.y, destinationPosition.x)
                    movementPerformed = true
                }
            }
        }
        return canMove
    }


    override fun attack(enemy: FighterModel): String {
        val heroPositionValue = position.y + position.x
        val enemyPositionValue = enemy.position.y + enemy.position.x
        val result: String

        if (abs(heroPositionValue - enemyPositionValue) == 1) {

            result = resolveAttack(enemy)

        } else {

            result = "${enemy.name} is so far"
        }
        return result
    }


    override fun shot(
        enemy: FighterModel,
        rocks: List<RockModel>,
        allFightersList: List<FighterModel>
    ): String {
        var difference: Int
        var resultOfDistanceDifference: Int
        var result = "${enemy.name} is to far"
        val cellsInTheBulletTrajectory =
            Bresenham.calculateBulletTrajectory(position, enemy.position)
        var thereIsRock = false
        var thereIsAnotherFighter = false

        for (rock in rocks) {
            for (cell in cellsInTheBulletTrajectory) {
                if (cell.y == rock.position.y && cell.x == rock.position.x) {
                    thereIsRock = true
                }
            }
        }

        Log.i("quique", "CHEQUEO DE HUMANOS EN MEDIO")
        for (fighter in allFightersList) {
            if (fighter.name != name && fighter.name != enemy.name) {
                Log.i(
                    "quique",
                    "Héroe ${fighter.name}, con posición: ${fighter.position.y}, ${fighter.position.x}"
                )
                Log.i("quique", "AHORA VIENEN LAS CELDAS A COMPARARSE CON LA POSICIÓN DEL HÉROE")
                for (cell in cellsInTheBulletTrajectory) {
                    Log.i("quique", "Posición de la celda ${cell.y}, ${cell.x}")
                    if (cell.y == fighter.position.y && cell.x == fighter.position.x) {
                        if (fighter.isHero != isHero) {
                            Log.i("quique", "${fighter.name} ESTÁ EN EL MEDIO")
                            thereIsAnotherFighter = true
                        } else if (abs((position.x + position.y) - (fighter.position.x + fighter.position.y)) > 1) {
                            thereIsAnotherFighter = true
                        }
                    }
                }
            }
        }


        if (!thereIsRock && !thereIsAnotherFighter) {
            with(position) {
                if (x < enemy.position.x && y < enemy.position.y) {
                    if (x + distanceToShot >= enemy.position.x) {
                        difference = enemy.position.x - x
                        resultOfDistanceDifference = distanceToShot - difference

                        if (y + resultOfDistanceDifference >= enemy.position.y) {

                            result = resolveShot(enemy)
                        }
                    }
                } else if (x > enemy.position.x && y > enemy.position.y) {
                    if (x - distanceToShot <= enemy.position.x) {
                        difference = x - enemy.position.x
                        resultOfDistanceDifference = distanceToShot - difference
                        if (y - resultOfDistanceDifference <= enemy.position.y) {

                            result = resolveShot(enemy)
                        }
                    }
                } else if (x < enemy.position.x && y > enemy.position.y) {
                    if (x + distanceToShot >= enemy.position.x) {
                        difference = enemy.position.x - x
                        resultOfDistanceDifference = distanceToShot - difference
                        if (y - resultOfDistanceDifference <= enemy.position.y) {

                            result = resolveShot(enemy)
                        }
                    }
                } else if (x > enemy.position.x && y < enemy.position.y) {
                    if (x - distanceToShot <= enemy.position.x) {
                        difference = x - enemy.position.x
                        resultOfDistanceDifference = distanceToShot - difference
                        if (y + resultOfDistanceDifference >= enemy.position.y) {


                            result = resolveShot(enemy)
                        }
                    }
                } else if (x == enemy.position.x && y < enemy.position.y) {
                    if (y + distanceToShot >= enemy.position.y) {


                        result = resolveShot(enemy)
                    }
                } else if (x == enemy.position.x && y > enemy.position.y) {
                    if (y - distanceToShot <= enemy.position.y) {


                        result = resolveShot(enemy)
                    }
                } else if (x < enemy.position.x && y == enemy.position.y) {
                    if (x + distanceToShot >= enemy.position.x) {


                        result = resolveShot(enemy)
                    }
                } else if (x > enemy.position.x && y == enemy.position.y) {
                    if (x - distanceToShot <= enemy.position.x) {

                        result = resolveShot(enemy)
                    }
                }
            }
        } else if (thereIsRock) {
            result = "The enemy is under cover"
        } else {
            result = "The enemy is behind other fighter"
        }

        return result
    }


    override fun sabotage(enemy: FighterModel): String {
        val heroPositionValue = position.y + position.x
        val enemyPositionValue = enemy.position.y + enemy.position.x
        val result: String

        if (abs(heroPositionValue - enemyPositionValue) == 1) {

            result = resolveSabotage(enemy)

        } else {

            result = "${enemy.name} is so far"
        }
        return result
    }

    override fun support(ally: FighterModel): String {
        val heroPositionValue = position.y + position.x
        val allyPositionValue = ally.position.y + ally.position.x
        val result: String

        if (abs(heroPositionValue - allyPositionValue) == 1) {

            result = resolveSupport(ally)

        } else {

            result = "${ally.name} is so far"
        }
        return result

    }

    override fun defense(): String {
        defenseBonus = 35
        actionPerformed = true
        movementPerformed = true
        return "$name is prepared for enemy attacks (+$defenseBonus combat)"
    }

    private fun resolveSabotage(enemy: FighterModel): String {
        val sabotageRoll = Random.nextInt(1, 101)
        val result: String

        if (sabotageRoll < 90 && sabotageRoll <= intelligence) {
            val sabotageDifference = intelligence - sabotageRoll

            val enemySabotageDifference = enemy.getIntelligenceRoll()

            if (sabotageDifference >= enemySabotageDifference) {

                enemy.isSabotaged = true

                result = "${enemy.name} was sabotaged and will lose the next turn"

            } else {
                result = "${enemy.name} was smarter and sabotage didn't work"
            }
        } else {
            result = "$name failed the try of sabotage"
        }

        actionPerformed = true
        return result
    }

    private fun resolveSupport(ally: FighterModel): String {
        val supportRoll = Random.nextInt(1, 101)
        val result: String

        if (supportRoll < 90 && supportRoll <= intelligence) {
            var supportDifference = intelligence - supportRoll

            if (supportDifference > 30) {
                supportDifference = 30
            }

            ally.combatBonus = supportDifference
            result = "${ally.name} is more powerful right now (+$supportDifference combat)"

        } else {
            result = "$name failed supporting ${ally.name}"
        }

        actionPerformed = true
        return result
    }

    private fun resolveAttack(enemy: FighterModel): String {
        score.meleeAtks++
        val attackRoll = Random.nextInt(1, 101)
        Log.i("diferencia", "La tirada de ataque A PALO SECO ES -> $attackRoll")
        Log.i("quique", "El valor de combat es -> $combat")
        val result: String

        if (attackRoll <= 90 && attackRoll <= combat + combatBonus) {
            val attackDifference = combat - attackRoll + combatBonus

            val enemyDefenseDifference = enemy.getDefenseRoll()
            Log.i(
                "diferencia",
                "La diferencia del enemigo de defensa es -> $enemyDefenseDifference"
            )
            //Log.i("diferencia", "El combat del enemigo  es -> ${enemy.combat}")
            Log.i("diferencia", "La diferencia de ataque es -> $attackDifference")
            //Log.i("diferencia", "El combat  es -> $combat")
            if (attackDifference >= enemyDefenseDifference) {
                var damageBonus = attackDifference - enemyDefenseDifference
                if (damageBonus > 20) {
                    damageBonus = 20
                }
                result = resolveDamage(damageBonus, enemy)

            } else {
                enemy.durability -= 5
                enemy.score.meleeDmgRec += 5
                enemy.score.defAtks++
                score.meleeDmg += 5
                result = "${enemy.name} defended the attack and just received 5 of damage"
            }
        } else {
            result = "$name failed the attack"
        }

        actionPerformed = true
        return result
    }

    private fun resolveShot(enemy: FighterModel): String {
        score.rangeAtks++
        val shotRoll = Random.nextInt(1, 101)
        Log.i("quique", "resultado de la tirada de disparo -> $shotRoll")
        Log.i("quique", "El valor de power es -> $power")
        val result: String
        actionPerformed = true
        if (shotRoll <= 90 && shotRoll <= power) {
            val shotDifference = power - shotRoll

            val enemySpeedDifference = enemy.getDodgeRoll()
            Log.i("quique", "La tiradad del enemigo de speed es -> $enemySpeedDifference")
            if (shotDifference >= enemySpeedDifference) {
                var damage = (shotDifference - enemySpeedDifference) / 5
                if (damage < 1) {
                    damage = 1
                }
                enemy.durability -= damage
                enemy.score.rangeDmgRec += damage
                score.rangeDmg += damage
                result = "$name inflicted $damage of damage to ${enemy.name}"


            } else {
                enemy.score.dodgedAtks++
                result = "${enemy.name} dodged the shot and don't received damage"
            }
        } else {
            result = "$name failed the shot"

        }

        return result
    }


    private fun resolveDamage(damageBonus: Int, enemy: FighterModel): String {
        val damageRoll = Random.nextInt(1, 101)
        Log.i("quique", "La tiradad de daño es -> $damageRoll")
        Log.i("quique", "La fuerza es -> $strength")
        val result: String
        if (damageRoll <= strength + damageBonus) {
            var damage = strength - damageRoll
            Log.i("quique", "El daño final es -> $damage")
            if (damage < 10) {
                damage = Random.nextInt(10, 16)
            }
            enemy.durability -= damage
            enemy.score.meleeDmgRec += damage
            score.meleeDmg += damage
            result = "$name inflicted $damage of damage to ${enemy.name}"

        } else {
            enemy.durability -= 8
            enemy.score.meleeDmgRec += 8
            score.meleeDmg += 8
            result = "$name didn't used strength enough and just caused 8 of damage"
        }
        return result
    }


    override fun getDefenseRoll(): Int {
        val defenseRoll = Random.nextInt(1, 101)
        Log.i("diferencia", "La tiradad del enemigo A PALO SECO de defensa es -> $defenseRoll")
        Log.i("diferencia", "el combatBonus es -> $combatBonus")
        Log.i("diferencia", "el defenseBonus es -> $defenseBonus")
        Log.i("diferencia", "El combat del defensor es de -> $combat")
        if (defenseRoll <= 90 && defenseRoll < combat + defenseBonus + combatBonus) {
            val difference = combat - defenseRoll + defenseBonus + combatBonus
            Log.i("diferencia", "El RESULTADO DE LA DIFERENCIA ANTES DE DEVOLVERLA -> $difference")
            return difference

        } else {
            return 0
        }
    }

    override fun getIntelligenceRoll(): Int {
        val intelligenceRoll = Random.nextInt(1, 101)
        return if (intelligenceRoll < 90 && intelligenceRoll < intelligence) {
            val difference = intelligence - intelligenceRoll
            difference
        } else {
            0
        }
    }

    override fun getDodgeRoll(): Int {
        val speedRoll = Random.nextInt(1, 101)
        Log.i("quique", "La tiradad del enemigo de defensa es -> $speedRoll")
        Log.i("quique", "El combat del defensor es de -> $combat")
        return if (speedRoll < 90 && speedRoll < speed + defenseBonus) {
            val difference = speed - speedRoll + defenseBonus
            difference
        } else {
            0
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