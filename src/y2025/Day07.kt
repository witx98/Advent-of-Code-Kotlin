package y2025

import Direction
import Grid
import Point
import println
import readInputLines


fun main() {
    val lines = readInputLines(2025, "day-07-input")
    val firstResult = firstPart(lines)
    val secondResult = secondPart(lines)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(lines: List<String>): Long {
    val grid = Grid(lines)

    val start = grid.indices.first { point -> grid.getAt(point) == 'S' }
    val cache = mutableMapOf<Point, Long>()
    return countTimelines(start, grid, cache)
}

private fun countTimelines(wall: Point, grid: Grid, cache: MutableMap<Point, Long>): Long {
    val cached = cache[wall]
    if (cached != null) {
        return cached
    }
    val next = wall.plus(Direction.DOWN)
    val result = when (grid.get(next)) {
        '.' -> countTimelines(next, grid, cache)
        '^' -> countTimelines(next.plus(Direction.LEFT), grid, cache) + countTimelines(next.plus(Direction.RIGHT), grid, cache)
        else -> 1
    }
    cache[wall] = result
    return result
}

private fun firstPart(lines: List<String>): Int {
    val grid = Grid(lines)

    val start = grid.indices.first { point -> grid.getAt(point) == 'S' }
    val walls = mutableSetOf(start)
    var counter = 0

    while (walls.isNotEmpty()) {
        val tempList = walls.toList()
        tempList.forEach { wall ->
            walls.remove(wall)
            val next = wall.plus(Direction.DOWN)
            when (grid.get(next)) {
                '.' -> walls.add(next)
                '^' -> {
                    counter++
                    val left = next.plus(Direction.LEFT)
                    val right = next.plus(Direction.RIGHT)
                    walls.add(left)
                    walls.add(right)
                }
                else -> return@forEach
            }
        }
    }

    return counter
}

