package y2024

import Point
import coordSequence
import println
import readInputLines
import kotlin.sequences.filter

fun main() {
    val input = readInputLines(2024, "day-20-input")
    firstPart(input).println()
    secondPart(input).println()
}

private fun firstPart(input: List<String>): Int {
    return countCheats(input, 2)
}

private fun secondPart(input: List<String>): Int {
    return countCheats(input, 20)
}

private fun countCheats(input: List<String>, maxCheatLen: Int, savedPicos: Int = 100): Int {
    val width = input[0].length
    val height = input.size
    val from = coordSequence(width, height).first { input[it] == 'S' }

    val map = input.map { it.replace('S', '.').replace('E', '.') }
    val nodes = path(map, from)

    return nodes.values.sumOf { start ->
        start.pos.neighborhoodCoords(maxCheatLen, width, height)
            .filter { it.distance(start.pos) <= maxCheatLen && it in nodes }
            .count { endCoord ->
                val endDist = nodes[endCoord]!!.distance
                val save = endDist - start.distance - start.pos.distance(endCoord)
                save >= savedPicos
            }
    }
}

private fun path(grid: List<String>, from: Point): Map<Point, PathNode> {
    val startNode = PathNode(from, 0)
    return generateSequence(startNode to startNode) { (pp, p) ->
        val next = p.pos.neighbors().find { it != pp.pos && grid[it] == '.' }
        next?.let { p to PathNode(it, p.distance + 1) }
    }.map { it.second }.associateBy { it.pos }
}

data class PathNode(val pos: Point, val distance: Int)

private fun Point.neighborhoodCoords(maxDist: Int, width: Int, height: Int): Sequence<Point> {
    val fromX = (x - maxDist).coerceAtLeast(1)
    val fromY = (y - maxDist).coerceAtLeast(1)
    val toX = (x + maxDist).coerceAtMost(width - 2)
    val toY = (y + maxDist).coerceAtMost(height - 2)
    return coordSequence(fromX..toX, fromY..toY)
}

operator fun List<String>.get(pos: Point): Char? = getOrNull(pos.y)?.getOrNull(pos.x)