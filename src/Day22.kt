fun main() {
    val input = readInputLines("day-22-input")

    firstPart(input).println()
    secondPart(input).println()
}

private fun firstPart(input: List<String>): Long {
    return input.sumOf { seed -> sequenceA(seed.toLong()).last() }

}

private fun secondPart(input: List<String>): Long {
    val (bestSequence, bestValue) = input.asSequence()
        .flatMap { sequenceB(it.toLong()).distinctBy { (d, _) -> d } }
        .groupingBy { (d, _) -> d  }.fold(
            initialValueSelector = { _, _ -> 0L },
            operation = { _, acc, (_, count) -> acc + count }
        ).maxBy { it.value }

    println(bestSequence)
    return bestValue
}

fun sequenceB(start: Long): Sequence<Pair<List<Int>, Int>> {
    return sequenceA(start)
        .map { "$it".last().digitToInt() }
        .zipWithNext { a, b -> b to (a - b) }
        .windowed(4) { list -> list.map { it.second } to list.last().first }
}

fun sequenceA(seed: Long): Sequence<Long> {
    return generateSequence(seed) { current ->
        val first = (current * 64).mix(current).prune()
        val second = (first / 32).mix(first).prune()
        val third = (second * 2048).mix(second).prune()
        third
    }.take(2001)
}

fun Long.mix(other: Long): Long = xor(other)
fun Long.prune(): Long = (this % 16777216)
