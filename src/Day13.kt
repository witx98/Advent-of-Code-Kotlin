data class Machine(val dA: Vec2, val dB: Vec2, val prize: Vec2)

fun main() {
    val machines: List<Machine> = machines(readInput("day-13-input"))
    firstPart(machines).println()
    secondPart(machines).println()
}

private fun machines(input: String): List<Machine> {
    val buttonRegex = Regex("""X\+(\d+), Y\+(\d+)""")
    val prizeRegex = Regex("""X=(\d+), Y=(\d+)""")
    return input.split("\n\n").map {
        it.lines().let { (a, b, p) ->
            val buttonA = buttonRegex.find(a)!!.destructured.let { (ax, ay) ->
                Vec2(ax.toLong(), ay.toLong())
            }
            val buttonB = buttonRegex.find(b)!!.destructured.let { (bx, by) ->
                Vec2(bx.toLong(), by.toLong())
            }
            val prize = prizeRegex.find(p)!!.destructured.let { (px, py) ->
                Vec2(px.toLong(), py.toLong())
            }
            Machine(buttonA, buttonB, prize)
        }
    }
}

private fun firstPart(machines: List<Machine>): Long {
    return solve(machines)
}

private fun secondPart(machines: List<Machine>): Long {
    return solve(machines.map { machine -> scale(machine) }.toList())
}

fun scale(machine: Machine): Machine {
    return with(machine) {
        val scalePrize = Vec2(prize.x + 10000000000000, prize.y + 10000000000000)
        Machine(dA, dB, scalePrize)
    }
}

fun solve(slots: List<Machine>): Long {
    return slots.sumOf {
        val (a, b) = getMinCoins(it)
        3 * a + b
    }
}

fun getMinCoins(machine: Machine): Pair<Long, Long> {
    with(machine) {
        val b = (dA.x * prize.y - dA.y * prize.x) / (dA.x * dB.y - dA.y * dB.x)
        val a = (prize.y - dB.y * b) / dA.y
        val check = dA.y * a + dB.y * b == prize.y && dA.x * a + dB.x * b == prize.x
        return if (check) Pair(a, b) else Pair(0, 0)
    }
}
