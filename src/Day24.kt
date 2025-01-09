fun main() {
    val input = readInput("day-24-input")
    CrossedWires(input).firstPart().println()
    CrossedWires(input).secondPart().println()
}

class CrossedWires(input: String) {
    private val initialValues: Map<String, Boolean> = buildMap {
        input.lineSequence().forEach { line ->
            val (key, value, _, _, _, _) = (pattern.matchEntire(line) ?: return@forEach).destructured
            if (value.isNotEmpty()) {
                put(key, value.toInt() != 0)
            }
        }
    }

    private val connections: Map<String, Gate> = buildMap {
        input.lineSequence().forEach { line ->
            val (_, _, leftInput, oper, rightInput, output) = (pattern.matchEntire(line) ?: return@forEach).destructured
            if (oper.isNotEmpty()) {
                put(output, Gate.create(leftInput, BinaryOperator.valueOf(oper), rightInput))
            }
        }
    }

    fun firstPart() = buildMap {
        putAll(initialValues)
        val evaluate  = DeepRecursiveFunction<String, Boolean> { key ->
            getOrPut(key) {
                val (lhs, op, rhs) = connections.getValue(key)
                val left = callRecursive(lhs)
                val right = callRecursive(rhs)
                when (op) {
                    BinaryOperator.AND -> left and right
                    BinaryOperator.OR -> left or right
                    BinaryOperator.XOR -> left xor right
                }
            }
        }
        connections.keys.forEach { evaluate(it) }
    }.filterKeys { it.startsWith("z") }
        .entries
        .sortedBy { it.key }
        .foldRight(0L) { (_, value), acc -> 2 * acc + if (value) 1 else 0 }

    fun secondPart() = buildSet {
        val connections = connections.entries.associateByTo(mutableMapOf(), { it.value }, { it.key })
        var carry: String? = null
        for (z in this@CrossedWires.connections.keys.filter { it.startsWith("z") }.sorted()) {
            val x = z.replaceFirstChar { 'x' }
            val y = z.replaceFirstChar { 'y' }
            if (carry != null) {
                var halfAdd = connections[Gate.create(x, BinaryOperator.XOR, y)]
                if (halfAdd == null) {
                    if (carry != z) {
                        add(carry)
                        add(z)
                        connections.swap(carry, z)
                    }
                    carry = null
                } else {
                    val fullAdd = connections[Gate.create(halfAdd, BinaryOperator.XOR, carry)] ?: run {
                        val alternative = connections.getValue(Gate.create(x, BinaryOperator.AND, y))
                        add(halfAdd!!)
                        add(alternative)
                        connections.swap(halfAdd!!, alternative)
                        halfAdd = alternative
                        connections.getValue(Gate.create(alternative, BinaryOperator.XOR, carry!!))
                    }
                    if (fullAdd != z) {
                        add(fullAdd)
                        add(z)
                        connections.swap(fullAdd, z)
                    }
                    carry = connections[Gate.create(x, BinaryOperator.AND, y)]?.let { overflow1 ->
                        connections[Gate.create(halfAdd!!, BinaryOperator.AND, carry!!)]?.let { overflow2 ->
                            connections[Gate.create(overflow1, BinaryOperator.OR, overflow2)]
                        }
                    }
                }
            } else {
                val add = connections.getValue(Gate.create(x, BinaryOperator.XOR, y))
                if (add != z) {
                    add(add)
                    add(z)
                    connections.swap(add, z)
                }
                carry = connections[Gate.create(x, BinaryOperator.AND, y)]
            }
        }
    }.sorted().joinToString(",")

    private enum class BinaryOperator {
        AND, OR, XOR
    }

    private data class Gate(val lhs: String, val operator: BinaryOperator, val rhs: String) {
        companion object {
            fun create(lhs: String, operator: BinaryOperator, rhs: String): Gate {
                return if (lhs <= rhs) Gate(lhs, operator, rhs) else Gate(rhs, operator, lhs)
            }
        }
    }

    companion object {
        private val pattern = """(\w+): ([01])|(\w+) (AND|OR|XOR) (\w+) -> (\w+)""".toRegex()

        private fun <K, V> MutableMap<K, V>.swap(u: V, v: V) {
            forEach { (key, value) ->
                when (value) {
                    u -> this[key] = v
                    v -> this[key] = u
                }
            }
        }
    }
}

