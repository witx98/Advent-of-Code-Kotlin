fun main() {
    val inputLines = readInput("day-05-input")
    val (rules, pages) = getRulesAndPages(inputLines)
    val (correctPages, incorrectPages) = groupPages(rules, pages)
    firstPart(correctPages).println()
    secondPart(incorrectPages, rules).println()
}

fun groupPages(rules: Map<Int, List<Int>>, pages: List<List<Int>>): Pair<List<List<Int>>, List<List<Int>>> {
    return pages.partition { page ->
        isCorrectPage(page, rules).first
    }
}

private fun getRulesAndPages(inputLines: List<String>): Pair<Map<Int, List<Int>>, List<List<Int>>> {
    val (rulesLines, pagesLines) = inputLines.filter { it.isNotBlank() }.partition { '|' in it }

    val rules = rulesLines.map {
        val (left, right) = it.trim().split("|").map(String::toInt)
        left to right
    }.groupBy({ it.first }, { it.second })

    val pages = pagesLines.map { it.trim().split(",").map(String::toInt) }

    return rules to pages
}

private fun firstPart(pages: List<List<Int>>): Int {
    return pages.sumOf { page -> page[page.size / 2] }
}

fun secondPart(incorrectPages: List<List<Int>>, rules: Map<Int, List<Int>>): Int {
    return incorrectPages
        .map { page -> correctPage(page.toMutableList(), rules) }
        .sumOf { correctedPage -> correctedPage[correctedPage.size / 2] }
}

fun correctPage(incorrectPage: MutableList<Int>, rules: Map<Int, List<Int>>): List<Int> {
    var (isCorrect, indices) = isCorrectPage(incorrectPage, rules)
    while (!isCorrect) {
        val (first, second) = indices

        swap(incorrectPage, first, second)
        val (newCheck, newIndices) = isCorrectPage(incorrectPage, rules)

        isCorrect = newCheck
        indices = newIndices
    }
    return incorrectPage
}

private fun swap(correctedPage: MutableList<Int>, first: Int, second: Int) {
    val temp = correctedPage[first]
    correctedPage[first] = correctedPage[second]
    correctedPage[second] = temp
}

fun isCorrectPage(page: List<Int>, rules: Map<Int, List<Int>>): Pair<Boolean, Pair<Int, Int>> {
    for (i in page.indices) {
        for (j in 0 until i) {
            if (page[j] in rules.getOrDefault(page[i], listOf())) {
                return false to (i to j)
            }
        }
    }
    return true to (0 to 0)
}
