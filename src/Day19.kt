fun main() {
    val (first, second) = readInput("day-19-input").split("\n\n")
    val towels = first.split(", ").toList()
    val patterns = second.lines()
    firstPart(towels, patterns).println()
    secondPart(towels, patterns).println()
}


private fun firstPart(towels: List<String>, patterns: List<String>): Int {
    return patterns.count {
        isPossible(towels, it)
    }
}

private fun isPossible(towels: List<String>, pattern: String): Boolean {
    if (pattern.isBlank()) return true
    for (towel in towels) {
        if (towel.length <= pattern.length) {
            val part = pattern.substring(0, towel.length)
            if (part == towel) {
                if (isPossible(towels, pattern.substring(towel.length))) {
                    return true
                }
            }
        }
    }
    return false
}

private fun secondPart(towels: List<String>, patterns: List<String>): Long {
    return patterns.sumOf {
        allPossibleCombinations(towels, it, mutableMapOf())
    }
}

private fun allPossibleCombinations(towels: List<String>, pattern: String, cache: MutableMap<String, Long>): Long {
    return cache.getOrPut(pattern) {
        if (pattern.isEmpty()) {
            1L
        } else {
            towels.filter { towel -> pattern.startsWith(towel) }.sumOf { towel ->
                allPossibleCombinations(towels, pattern.substring(towel.length), cache)
            }
        }
    }
}
