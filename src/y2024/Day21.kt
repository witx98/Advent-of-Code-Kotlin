package y2024

import Direction
import Direction.Companion.BASIC_DIRECTIONS
import GenericGrid
import Point
import println
import readInputLines
import y2024.Tile.*
import java.util.*

fun main() {
    val input = readInputLines(2024, "day-21-input")
    firstPart(input).println()
    secondPart(input).println()
}

private fun firstPart(input: List<String>): Long {
    val bottomLayer = buildLayers(2)
    return input.sumOf { line ->
        getIntValue(line) * bottomLayer.getShortestLength(line)
    }
}

private fun secondPart(input: List<String>): Long {
    val bottomLayer = buildLayers(25)
    return input.sumOf { line ->
        getIntValue(line) * bottomLayer.getShortestLength(line)
    }
}

private enum class Tile(var char: Char) {
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
        fun from(c: Char) = entries.first { it.char == c }
    }
}

private fun Direction.toChar(): Char {
    return when (this) {
        Direction.UP -> Tile.UP.char
        Direction.DOWN -> Tile.DOWN.char
        Direction.LEFT -> Tile.LEFT.char
        Direction.RIGHT -> Tile.RIGHT.char
        else -> throw IllegalArgumentException("Unknown direction: $this")
    }
}

private data class KeySequence(val moves: List<Direction>, val current: Point) : Comparable<KeySequence> {
    override fun compareTo(other: KeySequence) = this.moves.size.compareTo(other.moves.size)

    fun add(direction: Direction) = KeySequence(moves + direction, current + direction)

    fun asString() = moves.joinToString("") {
        it.toChar().toString()
    }
}

private class Layer(private val keyboard: GenericGrid<Tile>, private val lowerLayer: Layer?) {

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
            var position = getPosition(Tile.A)
            var outputs = listOf("")

            input.forEach { c ->
                val targetPosition = getPosition(Tile.from(c))

                val moves = shortestPaths(position, targetPosition)
                outputs = outputs.flatMap { output ->
                        moves.map { path ->
                            output + path.asString()
                        }
                    }

                outputs = outputs.map { it + "A" }
                position = targetPosition
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
                    head == Tile.EMPTY_SPACE -> Unit
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

private fun numericGrid(): GenericGrid<Tile> = GenericGrid(
    width = 3,
    height = 4,
    fields = buildList {
        add(Tile.N7)
        add(Tile.N8)
        add(Tile.N9)
        add(Tile.N4)
        add(Tile.N5)
        add(Tile.N6)
        add(Tile.N1)
        add(Tile.N2)
        add(Tile.N3)
        add(Tile.EMPTY_SPACE)
        add(Tile.N0)
        add(Tile.A)
    },
)


private fun arrowGrid(): GenericGrid<Tile> = GenericGrid(
    width = 3,
    height = 2,
    fields = buildList {
            add(Tile.EMPTY_SPACE)
            add(Tile.UP)
            add(Tile.A)
            add(Tile.LEFT)
            add(Tile.DOWN)
            add(Tile.RIGHT)
    }
)

private fun buildLayers(n: Int): Layer {
    var currentLayer: Layer? = null

    repeat(n) {
        currentLayer = Layer(arrowGrid(), currentLayer)
    }

    return Layer(numericGrid(), currentLayer)
}


private fun getIntValue(line: String) = line.dropWhile { it == '0' }.removeSuffix("A").toInt()

