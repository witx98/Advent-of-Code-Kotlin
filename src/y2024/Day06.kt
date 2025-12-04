package y2024

import Direction
import Grid
import Point
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import readInputLines
import kotlin.time.measureTimedValue

fun main() {
    val input = readInputLines(2024, "day-06-input")
    val grid = Grid(input)

    val (result, duration) = measureTimedValue {
        firstPart(grid)
    }
    println("First part duration: $duration, and result: $result")

    val (result2, duration2) = measureTimedValue {
        secondPart(grid)
    }
    println("Second part duration: $duration2, and result: $result2")

    val (result3, duration3) = measureTimedValue {
        secondPartAsync(grid)
    }
    println("Second part async duration: $duration3, and result: $result3")

    val (result4, duration4) = measureTimedValue {
        secondPartParallel(grid)
    }
    println("Second part parallel duration: $duration4, and result: $result4")


}

private fun walk(grid: Grid, start: Point, obstacle: Point? = null): Set<Point>? {
    val visited = mutableSetOf<Pair<Point, Direction>>()
    var direction = Direction.UP
    var current = start

    fun move(currentPoint: Point): Point {
        var nextPoint = currentPoint + direction
        while (grid.isWithinBounds(nextPoint) && (grid.getAt(nextPoint) == '#' || nextPoint == obstacle)) {
            direction = direction.rotate(Direction.Rotation.RIGHT)
            nextPoint = currentPoint + direction
        }
        return nextPoint
    }

    while (grid.isWithinBounds(current)) {
        if (!visited.add(current to direction)) {
            return null
        }
        current = move(current)
    }
    return visited.mapTo(mutableSetOf()) { it.first }
}

private fun firstPart(grid: Grid): Int {
    val sp = grid.indices.first { grid.getAt(it) == '^' }
    return walk(grid, sp)!!.size
}

private fun secondPart(grid: Grid): Int {
    val sp = grid.indices.first { grid.getAt(it) == '^' }
    val visitedPoints = walk(grid, sp)!!
    return visitedPoints.count { obstacle ->
        obstacle != sp && walk(grid, sp, obstacle) == null
    }
}

private fun secondPartAsync(grid: Grid): Int {
    val sp = grid.indices.first { grid.getAt(it) == '^' }
    val visitedPoints = walk(grid, sp)!!

    return runBlocking {
        visitedPoints.map { obstacle ->
            async {
                obstacle != sp && walk(grid, sp, obstacle) == null
            }
        }.awaitAll().count { it }
    }
}


private fun secondPartParallel(grid: Grid): Int {
    val sp = grid.indices.first { grid.getAt(it) == '^' }
    val visitedPoints = walk(grid, sp)!!
    return visitedPoints.parallelStream()
        .filter { obstacle -> obstacle != sp }
        .map { obstacle -> walk(grid, sp, obstacle) }
        .filter { it == null }.count().toInt()
}