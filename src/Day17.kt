import kotlin.math.pow

data class Program(var a: Long, var b: Long, var c: Long, val instructions: List<Int>) {
    private var pointer = 0
    val output: MutableList<Int> = mutableListOf()

    private fun combo(operand: Int) = when(operand) {
        in 0..3 -> operand.toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> throw IllegalArgumentException("Invalid operand: $operand")
    }

    private fun execute() {
        val instruction = instructions[pointer]
        val operand = instructions[pointer + 1]
        when(instruction) {
            0 -> a /= 2.0.pow(combo(operand).toDouble()).toInt()
            1 -> b = b xor operand.toLong()
            2 -> b = combo(operand).toInt().mod(8L)
            3 -> if (a != 0L) pointer = operand - 2
            4 -> b = b xor c
            5 -> output += combo(operand).mod(8L).toInt()
            6 -> b = a / 2.0.pow(combo(operand).toDouble()).toInt()
            7 -> c = a / 2.0.pow(combo(operand).toDouble()).toInt()
        }
        pointer += 2
    }

    fun run() {
        while (pointer < instructions.size) {
            execute()
        }
    }
}

fun parseProgram(lines: List<String>): Program {
    val pattern = Regex("""(\d+).*?(\d+).*?(\d+).*?([\d,]+)""")
    val (a, b, c, i) = pattern.find(lines.joinToString(""))!!.destructured
    return Program(a.toLong(), b.toLong(), c.toLong(), i.split(",").map { it.toInt() })
}


fun main() {
    val input = readInputLines("day-17-input")
    val program = parseProgram(input)

    fun runCorrupted(a: Long): List<Int> {
        val corrupted = Program(a, 0, 0, program.instructions)
        corrupted.run()
        return corrupted.output
    }

    fun findInput(target: List<Int>): Long {
        var a = if (target.size  == 1) 0 else findInput(target.drop(1)) shl 3
        while (runCorrupted(a) != target) {
            a++
        }
        return a
    }

    program.run()
    println(program.output.joinToString(","))
    findInput(program.instructions).println()
}


