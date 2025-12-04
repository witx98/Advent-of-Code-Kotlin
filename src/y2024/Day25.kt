package y2024

import println
import readInput

fun main() {
    val input = readInput(2024,  "day-25-input")
    firstPart(parse(input)).println()
}

typealias Input = Pair<List<List<Int>>, List<List<Int>>>

fun firstPart(input: Input): Int = input.let { (keys, locks) ->
    locks.sumOf { lock ->
        keys.count { key -> key.zip(lock).all { (k, l) -> k >= l } }
    }
}

fun parse(text: String): Input = text.lines().chunked(8).let { chunks ->
    val keys = mutableListOf<List<Int>>()
    val locks = mutableListOf<List<Int>>()
    chunks.forEach { chunk ->
        val isLock = chunk[0] == "#####"
        val counts = (0..4).map { col ->
            chunk.filterNot { it.isBlank() }.count { row -> row[col] == if (isLock) '#' else '.' }
        }
        if (isLock) locks.add(counts) else keys.add(counts)
    }
    keys to locks
}