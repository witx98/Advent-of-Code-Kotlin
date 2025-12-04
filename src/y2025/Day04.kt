package y2025

import Grid
import Point
import println
import readInputLines


fun main() {
    val lines = readInputLines(2025, "day-04-input")
    val firstResult = firstPart(lines)
    val secondResult = secondPart(lines)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(lines: List<String>): Int {
    val grid = Grid(lines)
    val removedRolls = mutableSetOf<Point>()
    var amount = gatherRemovedRolls(grid, removedRolls)
    while (amount > 0) {
        amount = gatherRemovedRolls(grid, removedRolls)
    }

    return removedRolls.size
}

private fun firstPart(lines: List<String>): Int {
    val grid = Grid(lines)
    val removedRolls = mutableSetOf<Point>()
    return gatherRemovedRolls(grid, removedRolls)
}

private fun gatherRemovedRolls(grid: Grid, removedRolls: MutableSet<Point>): Int {
    val removedAmount = grid.indices.count { point ->
        if (grid.getAt(point) != '@') return@count false
        if (point in removedRolls) return@count false

        val neighborRollCount = point.neighbors(true).count { neighbor ->
            checkNeighbour(grid, neighbor, removedRolls)
        }
        val canBeRemoved = neighborRollCount < 4
        if (canBeRemoved) {
            removedRolls.add(point)
        }
        canBeRemoved
    }
    return removedAmount
}

private fun checkNeighbour(grid: Grid, neighbour: Point, removedRolls: Set<Point>): Boolean {
    return grid.isWithinBounds(neighbour) && grid.getAt(neighbour) == '@' && neighbour !in removedRolls
}
