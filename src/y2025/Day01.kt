package y2025

import println
import readInputLines
import kotlin.math.absoluteValue


fun main() {
    val lines = readInputLines(2025, "day-01-input")
    val firstResult = firstPart(lines)
    val secondResult = secondPart(lines)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(lines: List<String>): Int {
    var current = 50
    var counter = 0

    lines.forEach {
        val rotation = parseLine(it)
        val fullSpins = rotation.absoluteValue / 100
        counter += fullSpins

        val remainingRotation = rotation % 100
        val rotatedPosition = current + remainingRotation
        val finalPosition = rotatedPosition.mod(100)
        if (current != 0) {
            if (finalPosition != rotatedPosition || finalPosition == 0) {
                counter++
            }
        }
        current = finalPosition
    }
    return counter
}

private fun firstPart(lines: List<String>): Int {
    var current = 50
    var counter = 0
    lines.forEach {
        val rotation = parseLine(it)
        current = (current + rotation) % 100

        if (current == 0) {
            counter++
        }
    }
    return counter
}

private fun parseLine(line: String): Int {
    val value = line.substring(1).toInt()
    return when (line[0]) {
        'L' -> -value
        'R' -> value
        else -> throw IllegalArgumentException()
    }
}
