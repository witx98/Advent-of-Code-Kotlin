package y2025

import Position
import println
import readInputLines


fun main() {
    val positions = readInputLines(2025, "day-08-input")
        .map {
            val (x,y,z) = it.split(",")
            Position(x.toInt(),y.toInt(),z.toInt())
        }
    val firstResult = firstPart(positions)
    val secondResult = secondPart(positions)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(positions: List<Position>): Int {

    return 0
}

private fun firstPart(positions: List<Position>): Int {
    val connectionsSortedByDistance = connectionsSortedByDistance(positions)
    val circuits = findCircuits(positions, connectionsSortedByDistance)

    val biggestCircuits = circuits.sortedByDescending { it.size }.take(3)
    println(biggestCircuits)
    val result = biggestCircuits
        .map { it.size }
        .reduce { acc, next -> acc * next }
    return result
}

private fun findCircuits(
    positions: List<Position>,
    connectionsSortedByDistance: List<Pair<Position, Position>>
): MutableSet<Set<Position>> {
    val circuits = positions.map { setOf(it) }.toMutableSet()

    var iterations = 0
    for ((pos1, pos2) in connectionsSortedByDistance) {
        val circuit1 = circuits.first { pos1 in it }
        val circuit2 = circuits.first { pos2 in it }
        if (circuit1 == circuit2) continue
        circuits.remove(circuit1)
        circuits.remove(circuit2)
        circuits.add(circuit1 + circuit2)
        iterations++
        if (iterations == 9) break
    }
    return circuits
}

private fun connectionsSortedByDistance(positions: List<Position>): List<Pair<Position, Position>> {
    val distances =  mutableListOf<Pair<Position, Position>>()
    for (i in 0 until positions.size - 1) {
        for (j in i + 1 until positions.size) {
            distances += positions[i] to positions[j]
        }
    }
    return distances.sortedBy { (pos1, pos2) -> pos1.distance(pos2) }
}

