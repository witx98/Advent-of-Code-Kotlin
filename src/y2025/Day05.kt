package y2025

import Range
import println
import readInputLines


fun main() {
    val lines = readInputLines(2025, "day-05-input")
    val breakPoint = lines.indexOf("")
    val (first, second) = lines.take(breakPoint) to lines.drop(breakPoint + 1)
    val ranges: List<Range> = first.map { line ->
        val splitLine = line.split("-")
        Range(splitLine[0].toLong(), splitLine[1].toLong())
    }
    val ingredientIds: List<Long> = second.map { line -> line.toLong() }
    val firstResult = firstPart(ranges, ingredientIds)
    val secondResult = secondPart(ranges)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(ranges: List<Range>): Long {
    return mergeRanges(ranges).sumOf { range -> range.difference() }
}

fun mergeRanges(ranges: List<Range>): List<Range> {
    val sortedRanges = ranges.sortedBy { it.start }
    val result = mutableListOf(sortedRanges.first())

    sortedRanges.drop(1).forEach { range ->
        val previousRange = result.last()
        if (range.start <= previousRange.end) {
            result[result.lastIndex] = Range(previousRange.start, maxOf(previousRange.end, range.end))
        } else {
            result.add(range)
        }
    }
    return result
}

private fun firstPart(ranges: List<Range>, ingredientIds: List<Long>): Int {
    return ingredientIds.count { id -> ranges.any { it.contains(id) } }
}
