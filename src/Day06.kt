
fun main() {
    val input = readInput("day-06-input")
    val grid2 = Grid2(input)

    grid2.firstPart().println()
    grid2.secondPart().println()

}

private class Grid2(lines: List<String>) {
    private val grid: List<MutableList<Char>> = lines.map { line -> line.trim().toMutableList() }
    private val width: Int = grid.first().size
    private val height: Int = grid.size
    private val OBSTICLE: Char = '#'
    private val STARTING_POINT: Char = '^'
    private val POSSIBLE_DIRECTIONS: List<Direction> = listOf(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
    private val indices = {
        sequence {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    yield(Pair(x, y))
                }
            }
        }
    }
    private var currentDirection: Int = 0

    private fun currentDirection(): Direction {
        return POSSIBLE_DIRECTIONS[currentDirection]
    }

    private fun switchDirection(reset: Boolean = false) {
        if (reset) {
            currentDirection = 0
            return
        }
        currentDirection = (currentDirection + 1) % 4
    }

    private fun isWithinBounds(point: Pair<Int, Int>): Boolean = point.first in 0 until width && point.second in 0 until height

    private fun isObstacle(point: Pair<Int, Int>): Boolean = isWithinBounds(point) && grid[point.second][point.first] == OBSTICLE

    private fun isStartingPoint(point: Pair<Int, Int>): Boolean = isWithinBounds(point) && grid[point.second][point.first] == STARTING_POINT

    private fun nextPoint(currentPoint: Pair<Int, Int>): Pair<Int, Int> {
        val nextX = currentPoint.first + currentDirection().dx
        val nextY = currentPoint.second + currentDirection().dy
        return Pair(nextX, nextY)
    }

    private fun move(currentPoint: Pair<Int, Int>, obstacle: Pair<Int, Int>?): Pair<Int, Int> {
        var nextPoint = nextPoint(currentPoint)
        while(isObstacle(nextPoint) || nextPoint == obstacle) {
            switchDirection()
            nextPoint = nextPoint(currentPoint)
        }
        return nextPoint
    }

    private fun walk(start: Pair<Int, Int>, obstacle: Pair<Int, Int>? = null): Set<Pair<Int, Int>>? {
        val visited = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
        switchDirection(true)
        var current = start
        while (isWithinBounds(current)) {
            if(!visited.add(current to currentDirection())) {
                return null
            }
            current = move(current, obstacle)
        }
        return visited.mapTo(mutableSetOf()) { it.first }
    }

    fun firstPart(): Int {
        val sp = indices().first { isStartingPoint(it) }
        return walk(sp)!!.size
    }

    fun secondPart(): Int {
        val sp = indices().first { isStartingPoint(it) }
        val visitedPoints =  walk(sp)!!
        return visitedPoints.count { obstacle ->
            obstacle != sp && walk(sp, obstacle) == null
        }
    }

}