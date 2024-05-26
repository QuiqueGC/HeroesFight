package com.example.heroes_fight.data.utils

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.RockModel
import javax.inject.Inject
import kotlin.random.Random

class BoardManager @Inject constructor() {
    fun getAccessibleTiles(
        distanceToCalculate: Int,
        originPosition: Position
    ): Array<Array<Boolean?>> {
        val boardOfTilesToMark = cleanMarkedTilesOfBoard()

        Log.i("quique", "NUEVA RONDA")
        for (i in 0 until 10) {
            for (j in 0 until 9) {
                var difference: Int
                var result: Int
                with(originPosition) {
                    if (x < j && y < i) {
                        if (x + distanceToCalculate >= j) {
                            difference = j - x
                            result = distanceToCalculate - difference

                            if (y + result >= i) {
                                boardOfTilesToMark[i][j] = true
                            }
                        }
                    } else if (x > j && y > i) {
                        if (x - distanceToCalculate <= j) {
                            difference = x - j
                            result = distanceToCalculate - difference
                            if (y - result <= i) {
                                boardOfTilesToMark[i][j] = true
                            }
                        }
                    } else if (x < j && y > i) {
                        if (x + distanceToCalculate >= j) {
                            difference = j - x
                            result = distanceToCalculate - difference
                            if (y - result <= i) {
                                boardOfTilesToMark[i][j] = true
                            }
                        }
                    } else if (x > j && y < i) {
                        if (x - distanceToCalculate <= j) {
                            difference = x - j
                            result = distanceToCalculate - difference
                            if (y + result >= i) {
                                boardOfTilesToMark[i][j] = true
                            }
                        }
                    } else if (x == j && y < i) {
                        if (y + distanceToCalculate >= i) {
                            boardOfTilesToMark[i][j] = true
                        }
                    } else if (x == j && y > i) {
                        if (y - distanceToCalculate <= i) {
                            boardOfTilesToMark[i][j] = true
                        }
                    } else if (x < j) {
                        if (x + distanceToCalculate >= j) {
                            boardOfTilesToMark[i][j] = true
                        }
                    } else if (x > j) {
                        if (x - distanceToCalculate <= j) {
                            boardOfTilesToMark[i][j] = true
                        }
                    }
                }
            }
        }
        return boardOfTilesToMark
    }

    private fun cleanMarkedTilesOfBoard(): Array<Array<Boolean?>> {
        val boardOfTilesToMark = Array(10) { arrayOfNulls<Boolean>(9) }
        for (i in 0 until 10) {
            for (j in 0 until 9) {
                boardOfTilesToMark[i][j] = false
            }
        }
        return boardOfTilesToMark
    }


    fun getRocks(): MutableList<RockModel> {
        val rocks = mutableListOf<RockModel>()
        val randomQuantityRocks = Random.nextInt(8, 16)
        for (i in 1..randomQuantityRocks) {
            val randomY = Random.nextInt(1, 9)
            val randomX = Random.nextInt(0, 9)
            rocks.add(RockModel(Position(randomY, randomX)))
        }
        return rocks
    }
}