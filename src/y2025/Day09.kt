package y2025

import Point
import println
import readInputLines
import kotlin.math.abs


fun main() {
    val points =
        readInputLines(2025, "day-09-input").map { line -> line.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
//    val firstResult = firstPart(points)
    val secondResult = secondPart(points)

//    firstResult.println()
    secondResult.println()
}

private fun secondPart(points: List<Point>): Int {
    val rectangles = mutableSetOf<Rectangle>()
    for (i in 0 until points.size - 1) {
        for (j in i + 1 until points.size) {
            val a = points[i]
            val b = points[j]
            val rectangle = Rectangle(a, b)
            rectangles.add(rectangle)
        }
    }

    val pollygonWalls = points.zipWithNext().map { it -> Wall(it.first, it.second) }.toMutableList()
    pollygonWalls.add(Wall(points.last(), points.first()))
    val polygon = Polygon(pollygonWalls)
    val sortedRectangles = rectangles.sortedByDescending { rectangle -> rectangle.area }

    val filteredRectangles = sortedRectangles.filter { it -> !polygon.anyWallsIntersects(it) }

    val valid = filteredRectangles.filter { rectangle -> polygon.isRectangleWithin(rectangle) }

    println("Here")
    println(valid)
    valid.maxBy { rectangle -> rectangle.area }.println()
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
    val walls: Set<Wall> = buildSet {
        val c = Point(a.x, b.y)
        val d = Point(b.x, a.y)
        add(Wall(a, c))
        add(Wall(a, d))
        add(Wall(b, c))
        add(Wall(b, d))
    }

    fun top(): Wall {
        val filtered = walls.filter { wall -> !wall.isVertical() }
        return if (filtered.isEmpty()) {
            walls.maxBy { it.a.y }
        } else {
            filtered.maxBy { it.a.y }
        }
    }

    fun bottom(): Wall {
        val filtered = walls.filter { wall -> !wall.isVertical() }
        return if (filtered.isEmpty()) {
            walls.minBy { it.a.y }
        } else {
            filtered.minBy { it.a.y }
        }
    }

    fun right(): Wall {
        val filtered = walls.filter { wall -> wall.isVertical() }
        return if (filtered.isEmpty()) {
            walls.maxBy { it.a.x }
        } else {
            filtered.maxBy { it.a.x }
        }
    }

    fun left(): Wall {
        val filtered =  walls.filter { wall -> wall.isVertical() }
        return if (filtered.isEmpty()) {
            walls.minBy { it.a.x }
        } else {
            filtered.minBy { it.a.x }
        }
    }
}

private data class Polygon(val walls: List<Wall>) {

    val topMostWall: Wall = walls.filter { wall -> !wall.isVertical() }.maxBy { it.a.y }
    val bottomMostWall: Wall = walls.filter { wall -> !wall.isVertical() }.minBy { it.a.y }
    val rightMostWall: Wall = walls.filter { wall -> wall.isVertical() }.maxBy { it.a.x }
    val leftMostWall: Wall = walls.filter { wall -> wall.isVertical() }.minBy { it.a.x }

    fun anyWallsIntersects(rectangle: Rectangle): Boolean {
        return rectangle.walls.any { this.walls.any { polygonWall -> polygonWall.intersects(it) } }
    }

    fun isRectangleWithin(rectangle: Rectangle): Boolean {
        val startTop = rectangle.top().a
        val startBottom = rectangle.bottom().a
        val startLeft = rectangle.left().a
        val startRight = rectangle.right().a


        val biggestY = topMostWall.a.y + 1
        val smallestY = bottomMostWall.a.y - 1

        val biggestX = rightMostWall.a.x + 1
        val smallestX = leftMostWall.a.x - 1


        val topRay = Wall(startTop.plus(Direction.UP), Point(startTop.x, biggestY))
        val bottomRay = Wall(startBottom.plus(Direction.DOWN), Point(startBottom.x, smallestY))
        val rightRay = Wall(startRight.plus(Direction.LEFT), Point(biggestX, startRight.y))
        val leftRay = Wall(startLeft.plus(Direction.RIGHT), Point(smallestX, startLeft.y))

        if (rectangle.a == Point(2,3) || rectangle.b == Point(2,3) && rectangle.a == Point(9,5) || rectangle.b == Point(9,5)) {
            println(rectangle)
        }

        val isInTop = walls.any { wall -> topRay.touch(wall) }
        val isInBottom = walls.any { wall -> bottomRay.touch(wall) }
        val isInRight = walls.any { wall -> rightRay.touch(wall) }
        val inInLeft = walls.any { wall -> leftRay.touch(wall) }


        return isInTop && isInRight && isInBottom && inInLeft
    }
}

private data class Wall(val a: Point, val b: Point) {

    fun isVertical(): Boolean {
        return a.x == b.x
    }

    fun touch(other: Wall): Boolean {
        val thisVertical = this.isVertical()
        val otherVertical = other.isVertical()
        if (thisVertical && otherVertical) return false

        val vertical = if (thisVertical) this else other
        val horizontal = if (thisVertical) other else this

        val vx = vertical.a.x
        val hy = horizontal.a.y

        val vMinY = minOf(vertical.a.y, vertical.b.y)
        val vMaxY = maxOf(vertical.a.y, vertical.b.y)

        val hMinX = minOf(horizontal.a.x, horizontal.b.x)
        val hMaxX = maxOf(horizontal.a.x, horizontal.b.x)

        val intersects = vx in hMinX..hMaxX && hy in vMinY..vMaxY
        return intersects
    }

    fun intersects(other: Wall): Boolean {
        val thisVertical = this.isVertical()
        val otherVertical = other.isVertical()
        if (thisVertical && otherVertical) return false

        val vertical = if (thisVertical) this else other
        val horizontal = if (thisVertical) other else this

        val vx = vertical.a.x
        val hy = horizontal.a.y

        val vMinY = minOf(vertical.a.y, vertical.b.y)
        val vMaxY = maxOf(vertical.a.y, vertical.b.y)

        val hMinX = minOf(horizontal.a.x, horizontal.b.x)
        val hMaxX = maxOf(horizontal.a.x, horizontal.b.x)

        val intersects = vx in (hMinX + 1)..(hMaxX - 1) && hy in (vMinY + 1)..(vMaxY - 1)
        return intersects
    }
}