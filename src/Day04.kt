fun main() {
    val lines = readInput("day-04-input")
    val grid = Grid(lines)

    grid.partOne().println()
    grid.partTwo().println()
}

private class Grid(lines: List<String>) {
    private val grid: List<List<Char>> = lines.map { line -> line.toCharArray().toList() }
    private val columns = grid.first().size
    private val rows = grid.size
    private val indices = sequence {
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                yield(Pair(x, y))
            }
        }
    }

    private fun isWithinBounds(y: Int, x: Int, margin: UInt = 0u): Boolean =
        y in (0 + margin.toInt()) until (rows - margin.toInt())
                && x in (0 + margin.toInt()) until (columns - margin.toInt())

    fun partOne(): Int {
        return this.indices.sumOf { (x, y) -> checkAllDirections(x, y) }
    }

    private fun checkAllDirections(x: Int, y: Int): Int {
        return Direction.entries.count { direction -> checkDirection(x, y, direction) }
    }

    private fun checkDirection(x: Int, y: Int, direction: Direction): Boolean {
        var currentX = x
        var currentY = y

        for (letter in "XMAS") {
            if (!isWithinBounds(currentY, currentX) || grid[currentY][currentX] != letter) {
                return false
            }
            currentX += direction.dx
            currentY += direction.dy
        }
        return true
    }


    fun partTwo(): Int {
        return indices.filter { (x, y) -> isWithinBounds(y, x, 1u) }
            .filter { (x, y) -> grid[y][x] == 'A' }
            .count { (x, y) -> checkDiagonals(x, y) }
    }

    private fun checkDiagonals(x: Int, y: Int): Boolean {
        val firstWord = "" + getNeighbour(x, y, Direction.UP_LEFT) + grid[y][x] + getNeighbour(x, y, Direction.DOWN_RIGHT)
        val secondWord = "" + getNeighbour(x, y, Direction.DOWN_LEFT) + grid[y][x] + getNeighbour(x, y, Direction.UP_RIGHT)
        return isMAS(firstWord) && isMAS(secondWord)
    }

    private fun getNeighbour(x: Int, y: Int, direction: Direction): Char {
        return grid[y + direction.dy][x + direction.dx]
    }

    private fun isMAS(word: String): Boolean {
        return word == "MAS" || word.reversed() == "MAS"
    }
}
