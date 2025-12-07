package y2024

import Direction
import Grid
import Point
import println
import readInputLines
import kotlin.text.iterator

fun main() {
    val lines = readInputLines(2024, "day-04-input")
    val grid = Grid(lines)

    partOne(grid).println()
    partTwo(grid).println()
}

private fun partOne(grid: Grid): Int {
    return grid.indices.sumOf { point ->
        Direction.entries.count { direction ->
            checkDirection(grid, point, direction)
        }
    }
}

private fun checkDirection(grid: Grid, point: Point, direction: Direction): Boolean {
    var current = point

    for (letter in "XMAS") {
        if (!grid.within(current) || grid.getAt(current) != letter) {
            return false
        }
        current += direction
    }
    return true
}


private fun partTwo(grid: Grid): Int {
    return grid.indices.filter { point -> grid.within(point, 1u) }
        .filter { p -> grid.getAt(p) == 'A' }
        .count { point -> checkDiagonals(grid, point) }
}

private fun checkDiagonals(grid: Grid, p: Point): Boolean {
    val firstWord = "" + grid.getAt(p + Direction.UP_LEFT) + grid.getAt(p) + grid.getAt(p + Direction.DOWN_RIGHT)
    val secondWord = "" + grid.getAt(p + Direction.DOWN_LEFT) + grid.getAt(p) + grid.getAt(p + Direction.UP_RIGHT)
    return isMAS(firstWord) && isMAS(secondWord)
}

private fun isMAS(word: String): Boolean {
    return word == "MAS" || word.reversed() == "MAS"
}
