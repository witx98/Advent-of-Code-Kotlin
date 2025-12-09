package y2025

import Point
import println
import readInputLines
import kotlin.math.abs


fun main() {
    val points = readInputLines(2025, "day-09-input").map{ line -> line.split(",").let{ Point(it[0].toInt(), it[1].toInt()) }}
    val firstResult = firstPart(points)
    val secondResult = secondPart(points)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(lines: List<Point>): Int {

    return 0
}

private fun firstPart(points: List<Point>): Long {
    val rectangles = mutableSetOf<Rectangle>()
    for (i in 0 until points.size - 1) {
        for (j in i + 1 until points.size) {
            rectangles.add(Rectangle(points[i], points[j]))
        }
    }

    return rectangles.maxBy { rectangle -> rectangle.area }.area
}

private data class Rectangle(val a: Point, val b: Point) {
    val area: Long = (1 + abs(a.x - b.x)).toLong() * (1 + abs(a.y - b.y)).toLong()
}