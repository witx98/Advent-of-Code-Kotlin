fun main() {
    val lines = readInput("day-03-input")

    firstPart(lines).println()
    secondPart(lines).println()
}

private fun secondPart(lines: List<String>): Long {
    val doRegex = """do\(\)"""
    val dontRegex = """don't\(\)"""
    val mulRegex = """mul\((\d{1,3}),(\d{1,3})\)"""
    val regex = """$mulRegex|$doRegex|$dontRegex""".toRegex()

    var flag = true
    var sum = 0L
    lines.forEach { line ->
        regex.findAll(line).forEach { match ->
            when (match.value) {
                "do()" -> flag = true
                "don't()" -> flag = false
                else -> {
                    if (flag) {
                        val (first: String, second: String) = match.destructured
                        sum += first.toLong() * second.toLong()
                    }
                }
            }
        }
    }
    return sum
}

private fun firstPart(lines: List<String>): Long {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    return lines.sumOf { line ->
        regex.findAll(line).sumOf { match ->
            val (first: String, second: String) = match.destructured
            first.toLong() * second.toLong()
        }
    }
}
