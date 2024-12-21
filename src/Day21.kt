import Tile.*
import java.util.*

private val BASIC_DIRECTIONS = listOf(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)

fun main() {
    val input = readInputLines("day-21-input")
    firstPart(input).println()
    secondPart(input).println()
}

private fun firstPart(input: List<String>): Long {
    val layers = buildLayers(2)
    return input.sumOf { line ->
        getIntValue(line) * layers.getShortestLength(line)
    }
}

private fun secondPart(input: List<String>): Long {
    val layers = buildLayers(25)
    return input.sumOf { line ->
        getIntValue(line) * layers.getShortestLength(line)
    }
}

enum class Tile(var char: Char) {
    UP('^'),
    DOWN('v'),
    LEFT('<'),
    RIGHT('>'),
    A('A'),
    N0('0'),
    N1('1'),
    N2('2'),
    N3('3'),
    N4('4'),
    N5('5'),
    N6('6'),
    N7('7'),
    N8('8'),
    N9('9'),
    EMPTY_SPACE('#'),
    ;

    companion object {
        fun fromChar(c: Char) = Tile.entries.first { it.char == c }
    }
}

private fun Direction.toCharCommand(): Char {
    return when (this) {
        Direction.UP -> UP.char
        Direction.DOWN -> DOWN.char
        Direction.LEFT -> LEFT.char
        Direction.RIGHT -> RIGHT.char
        else -> throw IllegalArgumentException("Unknown direction: $this")
    }
}

private data class KeySequence(val moves: List<Direction>, val current: Point) : Comparable<KeySequence> {
    override fun compareTo(other: KeySequence) = this.moves.size.compareTo(other.moves.size)

    fun add(direction: Direction) = KeySequence(moves + direction, current + direction)

    fun asString() = moves.joinToString("") {
        it.toCharCommand().toString()
    }
}

class Layer(private val keyboard: GenericGrid<Tile>, private val lowerLayer: Layer?) {

    private val keySequenceCache = mutableMapOf<Pair<Point, Point>, List<KeySequence>>()
    private val movesCache = mutableMapOf<String, List<String>>()
    private val shortestCache = mutableMapOf<String, Long>()

    fun getShortestLength(input: String): Long =
        shortestCache.getOrPut(input) {
            input
                .split("A")
                .dropLast(1)
                .sumOf { innerMovelet ->
                    getMovesForInput("${innerMovelet}A").minOf { moves ->
                        lowerLayer?.getShortestLength(moves) ?: moves.length.toLong()
                    }
                }
        }

    private fun getMovesForInput(input: String): List<String> =
        movesCache.getOrPut(input) {
            var position = getPosition(A)
            var outputs = listOf("")

            input.forEach { c ->
                var targetPos = getPosition(Tile.fromChar(c))

                val moves = shortestPaths(position, targetPos)
                outputs =
                    outputs.flatMap { output ->
                        moves.map { path ->
                            output + path.asString()
                        }
                    }

                outputs = outputs.map { it + "A" }
                position = targetPos
            }
            outputs
        }

    private fun shortestPaths(position: Point, targetPos: Point): List<KeySequence> =
        keySequenceCache.getOrPut(position to targetPos) {
            val paths = PriorityQueue<KeySequence>()

            paths.add(KeySequence(listOf(), Point(0, 0)))

            val result = mutableListOf<KeySequence>()

            fun shortestLengthSoFar() = (result.firstOrNull()?.moves?.size ?: Int.MAX_VALUE)
            do {
                val path = paths.poll()
                val headPos = position + path.current
                val head = keyboard[headPos]
                when {
                    path.moves.size > shortestLengthSoFar() -> Unit
                    headPos == targetPos -> result.add(path)
                    head == null -> Unit
                    head == EMPTY_SPACE -> Unit
                    else ->
                        paths.addAll(
                            BASIC_DIRECTIONS.map {
                                path.add(it)
                            },
                        )
                }
            } while (paths.isNotEmpty())

            result
        }

    private fun getPosition(t: Tile) = keyboard.indices.first {
        keyboard[it] == t
    }
}

private fun numericGrid(): GenericGrid<Tile> {
    val grid = GenericGrid(
        width = 3,
        height = 4,
        fields = buildList {
            repeat(3 * 4) {
                add(EMPTY_SPACE)
            }
        }.toMutableList(),
    )

    grid[Point(0, 0)] = N7
    grid[Point(1, 0)] = N8
    grid[Point(2, 0)] = N9
    grid[Point(0, 1)] = N4
    grid[Point(1, 1)] = N5
    grid[Point(2, 1)] = N6
    grid[Point(0, 2)] = N1
    grid[Point(1, 2)] = N2
    grid[Point(2, 2)] = N3
    grid[Point(0, 3)] = EMPTY_SPACE
    grid[Point(1, 3)] = N0
    grid[Point(2, 3)] = A
    return grid
}

private fun arrowGrid(): GenericGrid<Tile> {
    val grid = GenericGrid(
        width = 3,
        height = 2,
        fields = buildList {
            repeat(3 * 2) {
                add(EMPTY_SPACE)
            }
        }.toMutableList(),
    )
    grid[Point(0, 0)] = EMPTY_SPACE
    grid[Point(1, 0)] = UP
    grid[Point(2, 0)] = A
    grid[Point(0, 1)] = LEFT
    grid[Point(1, 1)] = DOWN
    grid[Point(2, 1)] = RIGHT
    return grid
}

fun buildLayers(n: Int): Layer {
    var currentLayer: Layer? = null

    repeat(n) {
        currentLayer = Layer(arrowGrid(), currentLayer)
    }

    return Layer(numericGrid(), currentLayer)
}


private fun getIntValue(line: String) = line.dropWhile { it == '0' }.removeSuffix("A").toInt()

