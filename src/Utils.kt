import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(name: String) = readInput(name).lines()
fun readInput(name: String) = Path("src/data/$name.txt").readText().trim()

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

}

data class Vec2(val x: Long, val y: Long)


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

    fun getAt(p: Point): Char = grid[p.y][p.x]

    fun getAsInt(p: Point): Int = getAt(p).toString().toInt()

    fun isWithinBounds(point: Point, margin: UInt = 0u): Boolean =
        point.y in (0 + margin.toInt()) until (rows - margin.toInt())
                && point.x in (0 + margin.toInt()) until (columns - margin.toInt())

}
