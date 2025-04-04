package ru.vorobeij

import kotlin.random.Random

fun runGradleCheck(): Boolean {
    return Random.nextInt() % 2 == 0
}