package y2024

import readInputLines
import kotlin.math.absoluteValue

fun main() {
    val input = readInputLines(2024, "day-02-input")
    val firstResult = firstPart(input)
    val secondResult = secondPart(input)
    println(firstResult)
    println(secondResult)
}

private fun secondPart(lines: List<String>): Int {
    return lines.map { line ->
        val report = line.split(" ").map { it.toInt() }
        tryEveryCombination(report)
    }.count { it }
}

private fun firstPart(lines: List<String>): Int {
    return lines.map { line ->
        val report = line.split(" ").map { it.toInt() }
        isSafe(report)
    }.count { it }
}

private fun tryEveryCombination(report: List<Int>): Boolean {
    if (isSafe(report)) {
        return true
    }

    for (i in report.indices) {
        if (isSafe(report.toMutableList().apply { removeAt(i) })) {
            return true
        }
    }
    return false
}

private fun isSafe(report: List<Int>): Boolean {
    var safe = true
    var isIncreasing = true
    var isDecreasing = true

    for (i in 0..<report.size - 1) {
        val a = report[i]
        val b = report[i + 1]
        val difference = (a - b).absoluteValue
        safe = safe && (difference <= 3) && difference != 0

        if (a > b) {
            isIncreasing = false
        }

        if (a < b) {
            isDecreasing = false
        }
    }
    return safe && (isIncreasing || isDecreasing)
}