package y2025

import Position
import println
import readInputLines


fun main() {
    val positions = readInputLines(2025, "day-08-input")
        .map {
            val (x, y, z) = it.split(",")
            Position(x.toLong(), y.toLong(), z.toLong())
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
    connectionsSortedByDistance: List<Connection>
): MutableSet<Set<Position>> {
    val circuits = positions.map { setOf(it) }.toMutableSet()

    for ((pos1, pos2) in connectionsSortedByDistance.take(1000)) {
        val circuit1: Set<Position> = circuits.first { pos1 in it }
        val circuit2: Set<Position> = circuits.first { pos2 in it }
        if (circuit1 == circuit2) {
            continue
        }

        circuits.remove(circuit1)
        circuits.remove(circuit2)
        val merge = circuit1 + circuit2
        circuits.add(merge)
    }
    return circuits
}


private data class Connection(val pos1: Position, val pos2: Position) {
    val distance = pos1.distance(pos2)
}

private fun connectionsSortedByDistance(positions: List<Position>): List<Connection> {
    val distances = mutableListOf<Pair<Position, Position>>()
    for (i in 0 until positions.size - 1) {
        for (j in i + 1 until positions.size) {
            distances += positions[i] to positions[j]
        }
    }
    return distances
        .map { Connection(it.first, it.second) }
        .sortedBy { it.distance }
}

