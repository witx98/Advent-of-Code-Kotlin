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

private fun secondPart(ranges: List<Range>): Long {
    return ranges.sumOf { range -> invalidIdsInRange(range, true).sum() }
}

private fun firstPart(ranges: List<Range>): Long {
    return ranges.sumOf { range -> invalidIdsInRange(range, false).sum() }
}

private fun invalidIdsInRange(range: Range, partTwo: Boolean): List<Long> {
    val invalidIds = mutableListOf<Long>()
    for (id in range.elements) {
        val idAsString = id.toString()
        if (!partTwo && isDigitSequenceRepeatedTwice(idAsString)) {
            invalidIds.add(id)
        }

        if (partTwo && isDigitSequenceRepeatedAtLeastTwice(idAsString)) {
            invalidIds.add(id)
        }
    }
    return invalidIds
}

fun isDigitSequenceRepeatedAtLeastTwice(idAsString: String): Boolean {
    val maxAmountOfRepeats = idAsString.length / 2

    for (chunkSize in 1..maxAmountOfRepeats) {
        if (idAsString.length % chunkSize != 0) continue

        val digitChunks = idAsString.chunked(chunkSize)
        val repeated = digitChunks.all { chunk -> chunk == digitChunks[0] }
        if (repeated) return true
    }
    return false
}

fun isDigitSequenceRepeatedTwice(idAsString: String): Boolean {
    if (idAsString.length % 2 != 0) {
        return false
    }

    val middle = idAsString.length / 2
    val firstHalf = idAsString.take(middle)
    val secondHalf = idAsString.substring(middle)

    return firstHalf == secondHalf
}