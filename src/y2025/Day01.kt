package y2025

import println
import readInputLines


fun main() {
    val lines = readInputLines(2025, "day-01-input")
    val firstResult = firstPart(lines)
    val secondResult = secondPart(lines)

    firstResult.println()
    secondResult.println()
}

fun secondPart(lines: List<String>) {

}

fun firstPart(lines: List<String>): Int {
    var current = 50
    var counter = 0
    lines.forEach {
        val (first, second) = parseLine(it)
        current = if (first == 'L') {
            (current - second) % 100
        } else {
            (current + second) % 100
        }


        if (current == 0) {
            counter++
        }
    }
    return counter
}

fun parseLine(line: String): Pair<Char, Int> {
 return line[0] to line.substring(1).toInt()
}
