package y2024

import println
import readInputLines

fun main() {
    val lines = readInputLines(2024, "day-23-input")
    val connections = mutableMapOf<String, Set<String>>()
    lines.forEach { line ->
        val (first, second) = line.split("-")
        connections[first] = connections.getOrDefault(first, emptySet()) + second
        connections[second] = connections.getOrDefault(second, emptySet()) + first
    }

    firstPart(connections).println()
    secondPart(connections).println()
}

private fun firstPart(connections: Map<String, Set<String>>): Int {
    val sets = mutableSetOf<Set<String>>()
    connections.entries.forEach { (key, value) ->
        val set = value.toList()
        for (i in 0..set.lastIndex) {
            for (j in (i + 1)..set.lastIndex) {
                if (set[i] in connections.getValue(set[j])) {
                    sets.add(setOf(key, set[i], set[j]))
                }
            }
        }
    }
    return sets.count { s -> s.any { it.startsWith('t') } }
}

private fun secondPart(connections: Map<String, Set<String>>): String {
    val groups = mutableSetOf<Set<String>>()
    connections.entries.forEach { (key, value) ->
        var intersect = value + key
        value.forEach { v ->
            val vs = connections.getValue(v)
            val next = intersect.intersect(vs + v)
            if (next.size > 2) {
                intersect = next
            }
        }
        groups.add(intersect)
    }
    return groups.maxBy { it.size }.sorted().joinToString(",")
}