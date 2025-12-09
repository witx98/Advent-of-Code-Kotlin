package y2025

import Point
import println
import readInputLines
import kotlin.math.abs


fun main() {
    val points =
        readInputLines(2025, "day-09-input").map { line -> line.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
    val firstResult = firstPart(points)
    val secondResult = secondPart(points)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(points: List<Point>): Long {
    val rectangles = rectangles(points)

    val polygonWalls = points.zipWithNext().map { Wall(it.first, it.second) }.toMutableList()
    polygonWalls.add(Wall(points.last(), points.first()))
    val polygon = Polygon(polygonWalls)
    val sortedRectangles = rectangles.sortedByDescending { rectangle -> rectangle.area }

    val biggestRectangle = sortedRectangles
        .filter { rectangle -> !polygon.anyWallsIntersects(rectangle) }
        .filter { rectangle -> polygon.isRectangleWithin(rectangle) }
        .maxBy { rectangle -> rectangle.area }

    return biggestRectangle.area
}

private fun rectangles(points: List<Point>): Set<Rectangle> {
    val rectangles = mutableSetOf<Rectangle>()
    for (i in 0 until points.size - 1) {
        for (j in i + 1 until points.size) {
            rectangles.add(Rectangle(points[i], points[j]))
        }
    }
    return rectangles
}

private fun firstPart(points: List<Point>): Long {
    return rectangles(points).maxBy { rectangle -> rectangle.area }.area
}

private data class Rectangle(val a: Point, val b: Point) {
    val area: Long = (1 + abs(a.x - b.x)).toLong() * (1 + abs(a.y - b.y)).toLong()
    val c = Point(a.x, b.y)
    val d = Point(b.x, a.y)
    val walls: Set<Wall> = buildSet {
        add(Wall(a, c))
        add(Wall(a, d))
        add(Wall(b, c))
        add(Wall(b, d))
    }
}

private data class Polygon(val walls: List<Wall>) {
    fun anyWallsIntersects(rectangle: Rectangle): Boolean {
        return rectangle.walls.any { rectWall -> this.walls.any { polygonWall -> polygonWall.intersects(rectWall) } }
    }

    fun isRectangleWithin(rectangle: Rectangle): Boolean {
        return containsPoint(rectangle.a) &&
                containsPoint(rectangle.b) &&
                containsPoint(rectangle.c) &&
                containsPoint(rectangle.d)
    }

    fun containsPoint(p: Point): Boolean {
        var crosses = 0
        for (wall in walls) {
            if (wall.isInWall(p)) {
                return true
            }

            if (!wall.isVertical()) {
                continue
            }
            val verticalX = wall.a.x
            if (p.y in wall.minY..<wall.maxY && verticalX > p.x) {
                crosses++
            }
        }

        if (crosses % 2 == 0) {
            return false
        } else {
            return true
        }
    }
}

private data class Wall(val a: Point, val b: Point) {
    val minY = minOf(a.y, b.y)
    val maxY = maxOf(a.y, b.y)

    val minX = minOf(a.x, b.x)
    val maxX = maxOf(a.x, b.x)

    fun isInWall(p: Point): Boolean {
        return p.x in minX..maxX && p.y in minY..maxY
    }

    fun isVertical(): Boolean {
        return a.x == b.x
    }

    fun intersects(other: Wall): Boolean {
        val thisVertical = this.isVertical()
        val otherVertical = other.isVertical()
        if (thisVertical == otherVertical) {
            return false
        }

        val vertical = if (thisVertical) this else other
        val horizontal = if (thisVertical) other else this

        val vx = vertical.a.x
        val hy = horizontal.a.y

        val vMinY = vertical.minY
        val vMaxY = vertical.maxY

        val hMinX = horizontal.minX
        val hMaxX = horizontal.maxX

        return (vx in (hMinX + 1)..<hMaxX) && (hy in (vMinY + 1)..<vMaxY)
    }
}