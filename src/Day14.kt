data class Robot(val position: Point, val velocity: Point, val constraints: Point) {
    fun steps(n: Int = 1): Robot {
        var newPosition = position
        repeat(n) {
            newPosition = move(newPosition)
        }
        return Robot(newPosition, velocity, constraints)
    }

    private fun move(currentPosition: Point): Point {
        return Point((currentPosition.x + velocity.x).mod(constraints.x), (currentPosition.y + velocity.y).mod(constraints.y))
    }
}

fun main() {

    val robots: List<Robot> = readInputLines("day-14-input").map { line ->
        val regex = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""")
        regex.matchEntire(line)!!.destructured.let { (px, py, vx, vy) ->
            Robot(Point(px.toInt(), py.toInt()), Point(vx.toInt(), vy.toInt()), Point(101, 103))
        }
    }
    firstPart(robots).println()
    secondPart(robots).println()
}

private fun firstPart(robots: List<Robot>): Long {
    val simulatedRobots = robots.map { robot ->
        robot.steps(100)
    }.toList()

    val first = simulatedRobots.count { robot ->
        with(robot) {
            position.x in 0 until  constraints.x / 2 && position.y in 0 until constraints.y / 2
        }
    }
    val second = simulatedRobots.count { robot ->
        with(robot) {
            position.x in constraints.x / 2 + 1 until constraints.x && position.y in 0 until constraints.y / 2
        }
    }
    val third = simulatedRobots.count { robot ->
        with(robot) {
            position.x in 0 until  constraints.x / 2 && position.y in constraints.y / 2 + 1 until constraints.y
        }
    }
    val fourth = simulatedRobots.count { robot ->
        with(robot) {
            position.x in constraints.x / 2 + 1 until constraints.x && position.y in constraints.y / 2 + 1 until constraints.y
        }
    }

    return first.toLong() * second.toLong() * third.toLong() * fourth.toLong()
}

private fun secondPart(robots: List<Robot>): Long {
    var simulatedRobots = robots
    var counter = 0L
    while (robots.size != simulatedRobots.map { it.position }.toSet().size) {
        simulatedRobots = simulatedRobots.map { robot ->
            robot.steps()
        }.toList()
        counter++
    }
    return counter
}
