package com.example.heroes_fight.utils

import android.util.Log
import com.example.heroes_fight.data.domain.model.common.Position
import javax.inject.Inject

class BoardManager @Inject constructor() {
    fun getAccessibleTiles(
        distanceToCalculate: Int,
        originPosition: Position
    ): Array<Array<Boolean?>> {
        val boardOfTilesToMark = cleanBoardOfTilesToMark()

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

    private fun cleanBoardOfTilesToMark(): Array<Array<Boolean?>> {
        val boardOfTilesToMark = Array(10) { arrayOfNulls<Boolean>(9) }

        for (i in 0 until 10) {
            for (j in 0 until 9) {
                boardOfTilesToMark[i][j] = false
            }
        }
        return boardOfTilesToMark
    }
}