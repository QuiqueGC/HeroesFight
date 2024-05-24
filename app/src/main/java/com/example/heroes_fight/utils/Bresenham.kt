package com.example.heroes_fight.utils

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import kotlin.math.abs

object Bresenham {

    fun calculateBulletTrajectory(
        shooterPosition: Position,
        enemyPosition: Position
    ): List<Position> {
        val positions = mutableListOf<Position>()
        var shooterX1Var = shooterPosition.x
        var shooterY1Var = shooterPosition.y
        val differenceX = abs(enemyPosition.x - shooterPosition.x)
        val differenceY = abs(enemyPosition.y - shooterPosition.y)
        val sx = if (shooterPosition.x < enemyPosition.x) 1 else -1
        val sy = if (shooterPosition.y < enemyPosition.y) 1 else -1
        var err = differenceX - differenceY

        //añadido manual
        var loop = 0

        if (shooterPosition.y != enemyPosition.y && shooterPosition.x != enemyPosition.x) {
            while (true) {
                Log.i("quique", "x del cuadrado a pintar -> ${shooterX1Var}")
                Log.i("quique", "y del cuadrado a pintar -> ${shooterY1Var}")
                positions.add(Position(shooterY1Var, shooterX1Var))
                if (shooterX1Var == enemyPosition.x && shooterY1Var == enemyPosition.y) break
                val e2 = 2 * err
                if (e2 > -differenceY) {
                    err -= differenceY
                    shooterX1Var += sx
                }
                if (e2 < differenceX) {
                    err += differenceX
                    shooterY1Var += sy
                }
            }
            //el else if es añadido manual (y su if pertinente).
            // No es del señor Bresenhan
        } else if (shooterPosition.y == enemyPosition.y) {
            if (shooterPosition.x < enemyPosition.x) {
                do {
                    positions.add(Position(shooterY1Var, shooterX1Var))
                    loop++
                    shooterX1Var++
                } while (loop < differenceX)
            } else {
                do {
                    positions.add(Position(shooterY1Var, shooterX1Var))
                    loop++
                    shooterX1Var--
                } while (loop < differenceX)
            }
        } else {
            if (shooterPosition.y < enemyPosition.y) {
                do {
                    positions.add(Position(shooterY1Var, shooterX1Var))
                    loop++
                    shooterY1Var++
                } while (loop < differenceY)
            } else {
                do {
                    positions.add(Position(shooterY1Var, shooterX1Var))
                    loop++
                    shooterY1Var--
                } while (loop < differenceY)
            }
        }

        return positions
    }
}