import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(year: Int, name: String) = readInput(year, name).lines()
fun readInput(year: Int, name: String) = Path("src/data/y$year/$name.txt").readText().trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


enum class Direction(val dy: Int, val dx: Int) {
    UP(-1, 0),
    UP_RIGHT(-1, 1),
    RIGHT(0, 1),
    DOWN_RIGHT(1, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1),
    LEFT(0, -1),
    UP_LEFT(-1, -1);

    companion object {
        val BASIC_DIRECTIONS = setOf(UP, RIGHT, DOWN, LEFT)
    }

    fun rotate(rotation: Rotation): Direction {
        return entries[(ordinal + rotation.value).mod(entries.size)]
    }

    enum class Rotation(val value: Int) {
        LEFT(-2), RIGHT(2)
    }

}

data class Point(val x: Int, val y: Int) {
    operator fun plus(direction: Direction) = Point(x + direction.dx, y + direction.dy)
    operator fun minus(direction: Direction) = Point(x - direction.dx, y - direction.dy)
    operator fun times(scalar: Int) = Point(x * scalar, y * scalar)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    fun distance(other: Point): Int = abs(x - other.x) + abs(y - other.y)

    fun neighbors(withDiagonal: Boolean = false): List<Point> {
        return buildList {
            add(this@Point + Direction.UP)
            add(this@Point + Direction.RIGHT)
            add(this@Point + Direction.DOWN)
            add(this@Point + Direction.LEFT)
            if (withDiagonal) {
                add(this@Point + Direction.UP_RIGHT)
                add(this@Point + Direction.DOWN_RIGHT)
                add(this@Point + Direction.DOWN_LEFT)
                add(this@Point + Direction.UP_LEFT)
            }
        }
    }
}

fun coordSequence(width: Int, height: Int) = coordSequence(0 until width, 0 until height)

fun coordSequence(xRange: IntRange, yRange: IntRange) = yRange.asSequence().flatMap { y -> xRange.asSequence().map { x -> Point(x, y) } }

data class Vec2(val x: Long, val y: Long)

data class Range (val start: Long, val end: Long) {

    fun contains(value: Long) = value in start..end

    fun difference() = 1 + end - start

    val elements = sequence { yieldAll(start..end) }
}

class Grid(lines: List<String>) {
    private val grid: List<List<Char>> = lines.map { line -> line.toCharArray().toList() }
    private val columns = grid.first().size
    private val rows = grid.size
    val indices = sequence {
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                yield(Point(x, y))
            }
        }
    }

    fun print() {
        for (y in 0 until columns) {
            for (x in 0 until rows) {
                print(grid[y][x])
            }
            println()
        }
    }

    fun getAt(p: Point): Char = grid[p.y][p.x]

    fun get(at: Point) = if (outside(at)) null else getAt(at)

    fun getAsInt(p: Point): Int = getAt(p).toString().toInt()

    fun within(point: Point, margin: UInt = 0u): Boolean =
        point.y in (0 + margin.toInt()) until (rows - margin.toInt())
                && point.x in (0 + margin.toInt()) until (columns - margin.toInt())

    fun outside(point: Point) = !within(point)

}

class GenericGrid<T>(
    val width: Int,
    val height: Int,
    val fields: List<T>
) {
    val topLeft = Point(0, 0)
    val bottomRight = Point(width - 1, height - 1)

    val indices = sequence {
        for (y in 0 until height) {
            for (x in 0 until width) {
                yield(Point(x, y))
            }
        }
    }

    operator fun get(at: Point) = if (outside(at)) null else fields[at.x + at.y * width]

    fun within(it: Point) = it.y in 0 until height && it.x in 0 until width

    fun outside(it: Point) = !within(it)
}
