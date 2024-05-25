package com.example.heroes_fight.utils

import javax.inject.Inject

class BoardManager @Inject constructor() {

    val board = Array(10) { arrayOfNulls<Boolean>(9) }
}