package y2024

import Direction
import Grid
import Point
import println
import readInputLines
import kotlin.collections.getValue

typealias WideBox = Pair<Point, Point>
data class WideWarehouse(val robot: Point, val boxes: Set<WideBox>, val walls: Set<Point>) {
    private val boxPositions: Map<Point, WideBox> = boxes.flatMap { listOf(it.first to it, it.second to it) }.toMap()

    fun next(direction: Direction): WideWarehouse {
        val newRobot = robot + direction
        val movedBoxes = mutableSetOf<WideBox>()
        val oldBoxes = mutableSetOf<WideBox>()
        var currentPosition = setOf(newRobot)

        while (currentPosition.all { it in boxPositions } && currentPosition.isNotEmpty()) {
            if (direction == Direction.DOWN || direction == Direction.UP) {
                currentPosition += currentPosition.flatMap { boxPositions.getValue(it).toList() }
                val boxes = currentPosition.map { boxPositions.getValue(it) }
                oldBoxes += boxes
                movedBoxes += boxes.map { it.first + direction to it.second + direction }
                currentPosition = currentPosition.map { it + direction }.filter { it in boxPositions || it in walls }.toSet()
            } else {
                val f = currentPosition.single()
                val box = boxPositions.getValue(f)
                oldBoxes += box
                movedBoxes += box.first + direction to box.second + direction
                currentPosition = setOf(f + direction + direction)
            }
        }

        if (currentPosition.any { it in walls }) {
            return this
        }
        return WideWarehouse(newRobot, (boxes - oldBoxes) + movedBoxes, walls)
    }
}


data class Warehouse(val robot: Point, val boxes: Set<Point>, val walls: Set<Point>) {
    fun next(direction: Direction): Warehouse {
        val newRobot = robot + direction
        val movedBoxes = mutableSetOf<Point>()
        val oldBoxes = mutableSetOf<Point>()
        var currentPosition = newRobot
        while (currentPosition in boxes) {
            oldBoxes.add(currentPosition)
            movedBoxes.add(currentPosition + direction)
            currentPosition += direction
        }
        if (currentPosition in walls) {
            return this
        }
        return Warehouse(newRobot, (boxes - oldBoxes) + movedBoxes, walls)
    }

    fun wide(): WideWarehouse {
        return WideWarehouse(
            Point(robot.x * 2, robot.y),
            boxes.map { WideBox(Point(it.x * 2, it.y), Point((it.x * 2) + 1, it.y)) }.toSet(),
            walls.map { Point(it.x * 2, it.y) }.toSet() + walls.map { Point((it.x * 2) + 1, it.y) }.toSet()
        )
    }
}

fun main() {
    val input = readInputLines(2024, "day-15-input")
    val directions = input.takeLastWhile { it.isNotEmpty() }.joinToString("").map { toDirection(it) }
    val map = input.takeWhile { it.isNotEmpty() }.toList()
    val grid = Grid(map)

    firstPart(grid, directions).println()
    secondPart(grid, directions).println()
}

private fun prepareWarehouse(grid: Grid): Warehouse {
    val robot = grid.indices.find { grid.getAt(it) == '@' }!!
    val boxes = grid.indices.filter { grid.getAt(it) == 'O' }.toSet()
    val walls = grid.indices.filter { grid.getAt(it) == '#' }.toSet()
    return Warehouse(robot, boxes, walls)
}

private fun firstPart(grid: Grid, moves: List<Direction>): Int {
    var warehouse = prepareWarehouse(grid)

    for (move in moves) {
        warehouse = warehouse.next(move)
    }

    return warehouse.boxes.sumOf { it.x + 100 * it.y }
}

private fun secondPart(grid: Grid, moves: List<Direction>): Int {
    var wideWarehouse = prepareWarehouse(grid).wide()

    for (move in moves) {
        wideWarehouse = wideWarehouse.next(move)
    }

    return wideWarehouse.boxes.sumOf { it.first.x + 100 * it.first.y }
}

fun toDirection(c: Char): Direction {
    return when (c) {
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        else -> throw IllegalArgumentException("Unknown direction: $c")
    }

}