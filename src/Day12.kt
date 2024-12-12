fun main() {
    val lines = readInputLines("day-12-input")
    val grid = Grid(lines)
    firstPart(grid).println()
}

private val DIRECTIONS = listOf(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)

private fun firstPart(grid: Grid): Int {
    val visited = mutableSetOf<Point>()

    fun dfs(current: Point, garden: Char, area: MutableSet<Point>) {
        if (visited.contains(current) || !grid.isWithinBounds(current) || grid.getAt(current) != garden) {
            return
        }

        visited.add(current)
        area.add(current)

        for (direction in DIRECTIONS) {
            val newPoint = current.plus(direction)
            dfs(newPoint, garden, area)
        }
    }


    val areas: List<Set<Point>> = grid.indices.map { point ->
        val currentArea = mutableSetOf<Point>()
        dfs(point, grid.getAt(point), currentArea)
        currentArea
    }.filter { it.isNotEmpty() }.toList()


    fun calculatePerimeter(area: Set<Point>): Int {
        return area.sumOf { point ->
            DIRECTIONS.count { direction ->
                !area.contains(point.plus(direction))
            }
        }
    }

    return areas.sumOf { it.size * calculatePerimeter(it) }
}
