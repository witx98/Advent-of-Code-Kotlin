package y2024

import println
import readInputLines
import kotlin.math.abs


fun main() {
    val lines = readInputLines(2024, "day-01-input")
    val firstResult = firstPart(lines)
    val secondResult = secondPart(lines)

    firstResult.println()
    secondResult.println()
}


private fun firstPart(lines: List<String>): Long {
    val (left, right) = lines.map { line ->
        val first = line.substringBefore(" ").toLong()
        val second = line.substringAfterLast(" ").toLong()
        first to second
    }.unzip()

    return left.sorted().zip(right.sorted()).sumOf { (first, second) -> abs(first - second) }
}

private fun secondPart(lines: List<String>): Long {
    val (left, right) = lines.map { line ->
        val first = line.substringBefore(" ").toLong()
        val second = line.substringAfterLast(" ").toLong()
        first to second
    }.unzip()

    val frequency: Map<Long, Int> = right.groupingBy { it }.eachCount()

    return left.fold(0L) { acc, num -> acc + num * frequency.getOrDefault(num, 0) }
}
