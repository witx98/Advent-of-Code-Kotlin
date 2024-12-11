fun main() {
    val stones = readInput("day-11-input").split(" ").toList()
    stones.println()
    firstPart(stones).println()
    secondPart(stones).println()
}

private fun secondPart(input: List<String>): Long {
    val cache = mutableMapOf<Pair<Long, Int>, Long>()

    fun count(stone: Long, iteration: Int): Long {
        if (iteration < 1) {
            return 1
        }

        cache[stone to iteration]?.let {
            return it
        }

        return blink(stone).sumOf {
            count(it, iteration - 1)
        }.also {
            cache[stone to iteration] = it
        }
    }

    return input.map { it.toLong() }.sumOf { count(it, 75) }
}

fun blink(stone: Long): List<Long> = when {
    stone == 0L -> listOf(1)
    stone.toString().length % 2 == 0 -> {
        val whole = stone.toString()
        val len = whole.length
        val first = whole.substring(0, len / 2).toLong()
        val second = whole.substring(len / 2, len).toLong()
        listOf(first, second)
    }

    else -> listOf(stone * 2024L)
}

private fun firstPart(input: List<String>): Int {
    var stones = input
    repeat(25) {
        stones = blink(stones)
    }
    return stones.size
}

fun blink(stones: List<String>): List<String> {
    return buildList {
        stones.forEach { stone ->
            if (stone.toLong() == 0L) {
                add("1")
            } else if (stone.length % 2 == 0) {
                val firstHalf = stone.substring(0, stone.length / 2).toLong()
                val secondHalf = stone.substring(stone.length / 2).toLong()
                add(firstHalf.toString())
                add(secondHalf.toString())
            } else {
                add((stone.toLong() * 2024L).toString())
            }
        }
    }
}
