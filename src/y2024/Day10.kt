package y2024

import Direction.Companion.BASIC_DIRECTIONS
import Grid
import Point
import println
import readInputLines

fun main() {
    val lines = readInputLines(2024, "day-10-input")
    val g = Grid(lines)
    firstPart(g).println()
    secondPart(g).println()
}

private fun firstPart(grid: Grid): Int {
    val startingPoints = grid.indices.filter { grid.getAt(it) == '0' }

    return startingPoints.sumOf { startingPoint->
        val possiblePeaks = solveFirstMaze(startingPoint, grid)
        possiblePeaks.size
    }
}

private fun solveFirstMaze(startingPoint: Point, grid: Grid): Set<Point> {
    val visited = mutableSetOf<Point>()
    val peaks = mutableSetOf<Point>()

     fun dfs(previous: Point, current: Point) {
        if  (!grid.within(current) || grid.getAt(current) == '.' || (current != previous && grid.getAsInt(current) - grid.getAsInt(previous) != 1) || visited.contains(current)) {
            return
        }

        visited.add(current)

        if (grid.getAt(current) == '9') {
            peaks.add(current)
        }

        for (direction in BASIC_DIRECTIONS) {
            val newPoint = current.plus(direction)
            dfs(current, newPoint)
        }


    }
    dfs(startingPoint, startingPoint)
    return peaks
}


private fun secondPart(grid: Grid): Int {
    val startingPoints = grid.indices.filter { grid.getAt(it) == '0' }
    return startingPoints.sumOf { startingPoint-> solveSecondMaze(startingPoint, grid).size }
}

private fun solveSecondMaze(startingPoint: Point, grid: Grid): Set<List<Point>> {
    val visited = mutableSetOf<Point>()
    val paths = mutableSetOf<List<Point>>()

    fun dfs(previous: Point, current: Point, currentPath: MutableList<Point>) {
        if  (!grid.within(current) || grid.getAt(current) == '.' || (current != previous && grid.getAsInt(current) - grid.getAsInt(previous) != 1) || visited.contains(current)) {
            return
        }

        visited.add(current)
        currentPath.add(current)

        if (grid.getAt(current) == '9') {
            paths.add(currentPath)
        }

        for (direction in BASIC_DIRECTIONS) {
            val newPoint = current.plus(direction)
            dfs(current, newPoint, currentPath)
        }

        visited.remove(current)
        currentPath.remove(current)
    }
    dfs(startingPoint, startingPoint, mutableListOf())
    return paths
}
