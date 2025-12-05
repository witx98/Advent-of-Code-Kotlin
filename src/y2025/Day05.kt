package y2025

import Range
import println
import readInputLines


fun main() {
    val lines = readInputLines(2025, "day-05-input")
    val idx = lines.indexOf("")
    val (first, second) =
        if (idx == -1) lines to emptyList()
        else lines.take(idx) to lines.drop(idx + 1)
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
    val mergedRanges = mergeRanges(ranges)
    return mergedRanges.sumOf { range -> range.difference() }
}

fun mergeRanges(ranges: List<Range>): List<Range> {
    val sortedRanges = ranges.sortedBy { it.start }
    val result = mutableListOf(sortedRanges.first())

    for (currentRange in sortedRanges.drop(1)) {
        val previous = result.last()

        if (currentRange.start <= previous.end) {
            result[result.lastIndex] = Range(previous.start, maxOf(previous.end, currentRange.end))
        } else {
            result.add(currentRange)
        }
    }
    return result
}

private fun firstPart(ranges: List<Range>, ingredientIds: List<Long>): Int {
    return ingredientIds.count { id -> ranges.any { it.contains(id) } }
}
