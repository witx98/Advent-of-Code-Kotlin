fun main() {
    val input = readInput("day-06-input")
    val grid = Grid(input)

    firstPart(grid).println()
    secondPart(grid).println()
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