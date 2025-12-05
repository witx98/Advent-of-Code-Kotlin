package y2025

import Range
import println
import readInput


fun main() {
    val ranges = readInput(2025, "day-02-input")
        .split(",")
        .map {
            val (start, end) = it.split("-")
            Range(start.toLong(), end.toLong())
        }
    val firstResult = firstPart(ranges)
    val secondResult = secondPart(ranges)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(ranges: List<Range>): Int {
    return 0
}

private fun firstPart(ranges: List<Range>): Long {
    return ranges.sumOf { range -> invalidIdsInRange(range).sum() }
}

private fun invalidIdsInRange(range: Range): List<Long> {
    val invalidIds = mutableListOf<Long>()
    for (id in range.elements) {
        val idAsString = id.toString()

        if (idAsString.length % 2 != 0) {
            continue
        }

        val middle = idAsString.length / 2
        val firstHalf = idAsString.take(middle)
        val secondHalf = idAsString.substring(middle)

        if (firstHalf == secondHalf) {
            invalidIds.add(id)
        }
    }
    return invalidIds
}