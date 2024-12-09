const val FREE = Int.MIN_VALUE

fun main() {
    val input: List<Int> = readInput("day-09-input").map { c -> c.toString().toInt() }.toList()
    firstTask(input).println()
    secondTask(input).println()

}

fun secondTask(input: List<Int>): Long {
    val segments = getSegments(input)
    val defragmented = defragment(segments)
    return calculateScore(defragmented)
}

fun defragment(segments: List<Int>): List<Int> {
    val list = segments.toMutableList()
    var fileEnd = list.indexOfLast { it != FREE }

    while (fileEnd > 0) {
        val fileStart = list.subList(0, fileEnd).indexOfLast { it != list[fileEnd] } + 1
        val fileSize = fileEnd - fileStart + 1
        val freeSpaceIndex = findFreeSpace(list, fileStart, fileSize)

        if (freeSpaceIndex != -1) {
            for (f in 0 until fileSize) {
                swap(list, fileStart + f, freeSpaceIndex + f)
            }
        }
        fileEnd = list.subList(0, fileStart).indexOfLast { it != FREE }
    }

    return list
}

fun findFreeSpace(segments: List<Int>, fileStart: Int, fileSize: Int): Int {
    var start = 0
    var count = 0
    for (i in 0 until fileStart) {
        if (segments[i] == FREE) {
            if (count == 0) {
                start = i
            }
            count++
            if (count == fileSize) return start
        } else {
            count = 0
        }
    }
    return -1
}


private fun firstTask(input: List<Int>): Long {
    val segments = getSegments(input)
    val cleanedUpSpace = twoPointer(segments)
    return calculateScore(cleanedUpSpace)
}


private fun calculateScore(segments: List<Int>): Long {
    var sum = 0L
    segments.forEachIndexed { index, value ->
        if (value != FREE) {
            sum += index * value
        }
    }
    return sum
}

private fun getSegments(input: List<Int>): List<Int> {
    var i = 0
    return buildList {
        input.forEachIndexed { index, value ->
            if (index % 2 == 0) {
                repeat(value) { add(i) }
                i++
            } else {
                repeat(value) { add(FREE) }
            }

        }
    }
}

private fun twoPointer(segments: List<Int>): List<Int> {
    val list = segments.toMutableList()
    var left = segments.indexOfFirst { it == FREE }
    var right = segments.indexOfLast { it != FREE }

    while (left < right) {
        when {
            list[left] == FREE && list[right] != FREE -> {
                swap(list, left, right)
                left++
                right--
            }
            list[left] != FREE -> left++
            else -> right--
        }
    }
    return list
}

private fun swap(list: MutableList<Int>, first: Int, second: Int) {
    val temp = list[first]
    list[first] = list[second]
    list[second] = temp
}
