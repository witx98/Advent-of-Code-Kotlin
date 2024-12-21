import Direction.Rotation.LEFT
import Direction.Rotation.RIGHT
import java.util.*

fun main() {
    val input = readInputLines("day-16-input")
    val g = Grid(input)
    firstPart(g).println()
    secondPart(g).println()
}

private data class State(val point: Point, val direction: Direction)
private data class Path(val state: State, val distance: Int, val previous: List<State>)


private fun secondPart(grid: Grid): Int {
    val end = grid.indices.first { grid.getAt(it) == 'E' }
    val start = grid.indices.first { grid.getAt(it) == 'S' }

    val visited = mutableSetOf<State>()
    val unvisited = PriorityQueue<Path>(compareBy { it.distance }).apply {
        add(Path(State(start, Direction.RIGHT), 0, emptyList()))
    }
    val bestSeats = mutableSetOf(end)
    var shortestDistance = Int.MAX_VALUE


     fun generateNextPaths(
        grid: Grid,
        state: State,
        distance: Int,
        previous: List<State>
    ): List<Path> {
        return listOf(
            Path(State(state.point, state.direction.rotate(LEFT)), distance + 1000, previous + state),
            Path(State(state.point, state.direction.rotate(RIGHT)), distance + 1000, previous + state),
            Path(State(state.point.plus(state.direction), state.direction), distance + 1, previous + state)
        ).filter { isValidState(grid, it.state, visited) }
    }


    while (unvisited.isNotEmpty()) {
        val (state, distance, previous) = unvisited.poll()
        visited.add(state)
        if (state.point == end) {
            if (distance <= shortestDistance) {
                shortestDistance = distance
                bestSeats += previous.map { it.point }
            } else {
                break
            }
        }
        unvisited += generateNextPaths(grid, state, distance, previous)
    }

    return bestSeats.size
}


private fun firstPart(grid: Grid): Int {
    val start = grid.indices.first { grid.getAt(it) == 'S' }
    val end = grid.indices.first { grid.getAt(it) == 'E' }

    val visited = mutableSetOf<State>()
    val unvisited = PriorityQueue<Pair<State, Int>>(compareBy { it.second }).apply {
        add(State(start, Direction.RIGHT) to 0)
    }

    fun generateNextStates(grid: Grid, state: State, distance: Int): List<Pair<State, Int>> {
        return listOf(
            State(state.point, state.direction.rotate(LEFT)) to distance + 1000,
            State(state.point, state.direction.rotate(RIGHT)) to distance + 1000,
            State(state.point.plus(state.direction), state.direction) to distance + 1
        ).filter { isValidState(grid, it.first, visited) }
    }


    while (unvisited.isNotEmpty()) {
        val (state, distance) = unvisited.poll()
        visited.add(state)

        if (state.point == end) return distance

        unvisited.addAll(generateNextStates(grid, state, distance))
    }

    return -1
}

private fun isValidState(grid: Grid, state: State, visited: Set<State>): Boolean {
    return state !in visited && grid.getAt(state.point) != '#'
}
