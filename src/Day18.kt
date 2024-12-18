import java.util.*

private const val WIDTH = 71
private const val HEIGHT = 71
private val START = Point(0, 0)
private val END = Point(WIDTH - 1, HEIGHT - 1)

fun main() {
    val bytes = readInputLines("day-18-input").map { line ->
        val (left, right) = line.split(",")
        Point(left.toInt(), right.toInt())
    }.toSet()

    firstPart(bytes.take(1024).toSet()).println()
    secondPart(bytes).println()
}

private fun firstPart(bytes: Set<Point>): Int {
    val visited = mutableSetOf<Point>()
    val unvisited = PriorityQueue<Pair<Point, Int>>(compareBy { it.second }).apply { add(START to 0) }


    fun isValidState(point: Pair<Point, Int>): Boolean {
        with(point) {
            return first !in visited
                    && first !in bytes
                    && first.x >= 0
                    && first.y >= 0
                    && first.x <= END.x
                    && first.y <= END.y
                    && point !in unvisited
        }
    }

    while (unvisited.isNotEmpty()) {
        val (current, distance) = unvisited.poll()
        visited.add(current)

        if (current == END) return distance

        unvisited.addAll(generateNextStates(current, distance).filter { isValidState(it) })
    }

    return -1
}


private fun secondPart(bytes: Set<Point>): String {
    val obstacles = mutableMapOf<Point, Boolean>()

    fun isValidState(
        point: Pair<Point, Int>,
        unvisited: PriorityQueue<Pair<Point, Int>>,
        visited: Set<Point>
    ): Boolean {
        with(point) {
            return first !in visited
                    && obstacles[first] != true
                    && first.x >= 0
                    && first.y >= 0
                    && first.x <= END.x
                    && first.y <= END.y
                    && point !in unvisited
        }
    }

    for (point in bytes) {
        var reachable = false
        obstacles[point] = true
        val visited = mutableSetOf<Point>()
        val unvisited = PriorityQueue<Pair<Point, Int>>(compareBy { it.second }).apply { add(START to 0) }
        while (unvisited.isNotEmpty()) {
            val (current, distance) = unvisited.poll()
            visited.add(current)
            if (current == END) {
                reachable = true
                break
            }
            unvisited.addAll(generateNextStates(current, distance).filter { isValidState(it, unvisited, visited) })
        }
        if (!reachable) {
            return "${point.x},${point.y}"
        }
    }
    return "not found"
}


private fun generateNextStates(current: Point, distance: Int): List<Pair<Point, Int>> {
    return listOf(
        current + Direction.UP to distance + 1,
        current + Direction.RIGHT to distance + 1,
        current + Direction.DOWN to distance + 1,
        current + Direction.LEFT to distance + 1,
    )
}

