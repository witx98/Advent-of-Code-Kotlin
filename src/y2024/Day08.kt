package y2024

import Grid
import Point
import println
import readInputLines

fun main() {
    val readInput = readInputLines(2024, "day-08-input")
    val grid = Grid(readInput)

    firstPart(grid).println()
    secondPart(grid).println()
}

private fun firstPart(grid: Grid): Int {
    return findAntiNodes(lookup(grid), grid).size
}

private fun secondPart(grid: Grid): Int {
    return findAntiNodes2(lookup(grid), grid).size
}

fun findAntiNodes(lookup: Map<Char, Set<Point>>, grid: Grid): Set<Point> {
    return buildSet {
        lookup.keys.forEach { type ->
            for (first in lookup[type]!!) {
                for (second in lookup[type]!!) {
                    if (first != second) {
                        val antiNodePosition = first + (second - first) * 2
                        if (grid.isWithinBounds(antiNodePosition)) {
                            add(antiNodePosition)
                        }
                    }
                }
            }
        }
    }
}

fun findAntiNodes2(lookup: Map<Char, Set<Point>>, grid: Grid): Set<Point> {
    return buildSet {
        lookup.keys.forEach { type ->
            for (first in lookup[type]!!) {
                for (second in lookup[type]!!) {
                    if (first != second) {
                        var i = 0
                        var antiNodePosition = first + (second - first) * i
                        while (grid.isWithinBounds(antiNodePosition)) {
                            add(antiNodePosition)
                            i++
                            antiNodePosition = first + (second - first) * i
                        }
                    }
                }
            }
        }
    }
}

private fun lookup(grid: Grid): Map<Char, Set<Point>> {
    return buildMap<Char, MutableSet<Point>> {
        grid.indices.forEach { point ->
            val char = grid.getAt(point)
            if (char != '.') {
                val set = this.getOrElse(char) { mutableSetOf() }
                set.add(point)
                this[char] = set
            }
        }
    }
}