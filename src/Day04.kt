fun main() {
    val lines = readInput("day-04-input")
    val grid = Grid(lines)

    grid.countWordOccurrence("XMAS").println()
    grid.xmasCounter().println()
}


enum class Direction(val dy: Int, val dx: Int) {
    UP(-1, 0),
    UP_RIGHT(-1, 1),
    RIGHT(0, 1),
    DOWN_RIGHT(1, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1),
    LEFT(0, -1),
    UP_LEFT(-1, -1);
}

class Grid(lines: List<String>) {
    private val grid: List<List<Char>> = lines.map { line ->
        line.toCharArray().toList()
    }
    private val columns = grid.first().size
    private val rows = grid.size

    private fun isWithinBounds(y: Int, x: Int): Boolean = y in 0 until rows && x in 0 until columns

    fun countWordOccurrence(word: String): Int {
        val firstLetter = word[0]
        var counter = 0
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                if (grid[y][x] == firstLetter) {
                    counter += checkAllDirections(y, x, word)
                }
            }
        }
        return counter
    }

    fun xmasCounter(): Int {
        var counter = 0
        for (y in 1 until rows - 1) {
            for (x in 1 until columns - 1) {
                if (grid[y][x] == 'A') {
                    counter += checkDiagonals(y, x)
                }
            }
        }
        return counter
    }


    private fun checkDiagonals(y: Int, x: Int): Int {
        val firstWord = "" + grid[y - 1][x - 1] + grid[y][x] + grid[y + 1][x + 1]
        val secondWord = "" + grid[y - 1][x + 1] + grid[y][x] + grid[y + 1][x - 1]
        return if (isMAS(firstWord) && isMAS(secondWord)) 1 else 0
    }

    private fun isMAS(word: String): Boolean {
        return word == "MAS" || word.reversed() == "MAS"
    }

    private fun checkAllDirections(y: Int, x: Int, word: String): Int {
        val length = word.length
        var counter = 0
        for (direction in Direction.entries) {
            var currentY = y
            var currentX = x
            var match = true

            for (i in 1 until length) {
                currentY += direction.dy
                currentX += direction.dx
                if (!isWithinBounds(currentY, currentX) || grid[currentY][currentX] != word[i]) {
                    match = false
                    break
                }
            }
            if (match) {
                counter++
            }
        }
        return counter
    }
}
