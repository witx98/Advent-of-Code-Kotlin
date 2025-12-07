package y2024

import Direction.Companion.BASIC_DIRECTIONS
import Grid
import Point
import println
import readInputLines

fun main() {
    val lines = readInputLines(2024, "day-12-input")
    val grid = Grid(lines)
    firstPart(grid).println()
    secondPart(grid).println()
}


private fun firstPart(grid: Grid): Int {
    return getAreas(grid).sumOf { it.size * calculatePerimeter(it) }
}

private fun getAreas(grid: Grid): List<List<Point>> {
    val visited = mutableSetOf<Point>()

    fun dfs(current: Point, garden: Char, area: MutableList<Point>) {
        if (visited.contains(current) || !grid.within(current) || grid.getAt(current) != garden) {
            return
        }

        visited.add(current)
        area.add(current)

        for (direction in BASIC_DIRECTIONS) {
            val newPoint = current.plus(direction)
            dfs(newPoint, garden, area)
        }
    }

    return grid.indices.map { point ->
        val currentArea = mutableListOf<Point>()
        dfs(point, grid.getAt(point), currentArea)
        currentArea
    }.filter { it.isNotEmpty() }.toList()
}

fun calculatePerimeter(area: List<Point>): Int {
    return area.sumOf { point ->
        BASIC_DIRECTIONS.count { direction ->
            !area.contains(point.plus(direction))
        }
    }
}

private fun secondPart(grid: Grid): Int {
    return getAreas(grid).sumOf { sides(it) * it.size }
}

private fun sides(area: List<Point>): Int {
    val lookup: Set<Point> = area.toSet()
    return BASIC_DIRECTIONS.sumOf { direction ->
        var borderCount = 0
        val visited = mutableSetOf<Point>()
        for (point in area) {
            if (visited.contains(point)) {
                continue
            }
            if (point.plus(direction) !in lookup) {
                borderCount++
                visited.add(point)
                listOf(Direction.Rotation.LEFT, Direction.Rotation.RIGHT).map { direction.rotate(it) }
                    .forEach { sideDirection ->
                        var current = point
                        while (current in lookup && (current + direction) !in lookup) {
                            current += sideDirection
                            visited.add(current)
                        }
                    }
            }
        }
        borderCount
    }
}
