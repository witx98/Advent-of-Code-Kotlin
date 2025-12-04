package y2025

import Direction
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

private fun secondPart(lines: List<String>) {

}

private fun firstPart(lines: List<String>): Int {
    val grid = Grid(lines)
    return grid.indices.count { point ->
        if (grid.getAt(point) != '@') return@count false

        val neighborRollCount = Direction.entries.count { direction ->
            checkNeighbour(grid, point, direction)
        }
        neighborRollCount < 4
    }
}

private fun checkNeighbour(grid: Grid, point: Point, direction: Direction): Boolean {
    val neighbour = point + direction

    return grid.isWithinBounds(neighbour) && grid.getAt(neighbour) == '@'
}
