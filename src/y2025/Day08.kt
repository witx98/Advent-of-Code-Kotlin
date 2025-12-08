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

private fun secondPart(positions: List<Position>): Long {
    val connectionsSortedByDistance = connectionsSortedByDistance(positions)
    val (pos1, pos2) = findLastCircuit(positions, connectionsSortedByDistance)
    return pos1.x * pos2.x
}

private fun findLastCircuit(
    positions: List<Position>,
    connectionsSortedByDistance: List<Connection>
): Pair<Position, Position> {
    val circuits = positions.map { setOf(it) }.toMutableSet()
    for ((pos1, pos2) in connectionsSortedByDistance) {
        updateCircuits(circuits, pos1, pos2)
        if (circuits.size == 1) {
            return pos1 to pos2
        }
    }
    throw IllegalStateException("No last circuit found")
}

private fun updateCircuits(
    circuits: MutableSet<Set<Position>>,
    pos1: Position,
    pos2: Position
) {
    val circuit1: Set<Position> = circuits.first { pos1 in it }
    val circuit2: Set<Position> = circuits.first { pos2 in it }
    if (circuit1 == circuit2) {
        return
    }

    circuits.remove(circuit1)
    circuits.remove(circuit2)
    val merge = circuit1 + circuit2
    circuits.add(merge)
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
        updateCircuits(circuits, pos1, pos2)
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

