package com.example.randomizer

import kotlin.random.Random

class NumberRandomizer {

    fun generateNumbers(min: Int, max: Int, count: Int): List<Int> {
        return (1..count).map { Random.nextInt(min, max + 1) }
    }

    fun generateUniqueNumbers(min: Int, max: Int, count: Int): List<Int> {
        val range = (min..max).toList()
        return range.shuffled().take(count)
    }
}
