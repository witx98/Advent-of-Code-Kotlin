import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.math.pow
import kotlin.time.measureTimedValue

fun main() {
    val input = readInput("day-07-input")
    val retrieveEquations = retrieveEquations(input)
    firstPart(retrieveEquations).println()
    val (result, duration) = measureTimedValue {
        secondPartIterative(retrieveEquations)
    }
    println("Second part iterative duration: $duration, and result: $result")

    val (result2, duration2) = measureTimedValue {
        secondPartIterativeAsync(retrieveEquations)
    }
    println("Second part parallel iterative duration: $duration2, and result: $result2")

    val (result3, duration3) = measureTimedValue {
        secondPartIterativeParallel(retrieveEquations)
    }
    println("Second part async iterative duration: $duration3, and result: $result3")

    val (result4, duration4) = measureTimedValue {
        secondPartRecursive(retrieveEquations)
    }
    println("Second part recursive duration: $duration4, and result: $result4")

    val (result5, duration5) = measureTimedValue {
        secondPartRecursiveAsync(retrieveEquations)
    }
    println("Second part parallel recursive duration: $duration5, and result: $result5")

    val (result6, duration6) = measureTimedValue {
        secondPartRecursiveParallel(retrieveEquations)
    }
    println("Second part async recursive duration: $duration6, and result: $result6")
}

private fun secondPartIterative(equations: List<Pair<Long, List<Long>>>): Long {
    val operators = listOf("+", "*", "||")

    return equations.sumOf {
        isPossible(it.first, it.second, operators)
    }
}


private fun secondPartIterativeAsync(equations: List<Pair<Long, List<Long>>>): Long {
    val operators = listOf("+", "*", "||")

    return runBlocking(Dispatchers.Default) {
        equations.map {
            async {
                isPossible(it.first, it.second, operators)
            }
        }.awaitAll().sum()
    }
}

private fun secondPartIterativeParallel(equations: List<Pair<Long, List<Long>>>): Long {
    val operators = listOf("+", "*", "||")

    return equations.parallelStream().map {
        isPossible(it.first, it.second, operators)
    }.reduce(0L, Long::plus)
}

private fun secondPartRecursive(equations: List<Pair<Long, List<Long>>>): Long {
    return equations.filter { (target, numbers) -> target in recurse(numbers.first(), numbers.drop(1)) }
        .sumOf { (target, _) -> target }
}

private fun secondPartRecursiveAsync(equations: List<Pair<Long, List<Long>>>): Long {
    return runBlocking(Dispatchers.Default) {
        equations.map { (target, numbers) ->
            async {
                if(target in recurse(numbers.first(), numbers.drop(1))) target else 0L
            }
        }.awaitAll().sum()
    }
}

private fun secondPartRecursiveParallel(equations: List<Pair<Long, List<Long>>>): Long {
    return equations.parallelStream().map { (target, numbers) ->
        if (target in recurse(numbers.first(), numbers.drop(1))) target else 0L
    }.reduce(0L, Long::plus)
}

private fun recurse(result: Long, numbers: List<Long>): List<Long> {
    if (numbers.isEmpty()) return listOf(result)
    return recurse(result + numbers.first(), numbers.drop(1)) +
            recurse(result * numbers.first(), numbers.drop(1)) +
            recurse("$result${numbers.first()}".toLong(), numbers.drop(1))
}

private fun firstPart(equations: List<Pair<Long, List<Long>>>): Long {
    val operators = listOf("+", "*")
    return equations.sumOf { (result, numbers) ->
        isPossible(result, numbers, operators)
    }
}

private fun retrieveEquations(input: List<String>) = input.map { line ->
    val (first, second) = line.trim().split(":")
    first.toLong() to second.trim().split(" ").map { it.toLong() }
}

fun isPossible(result: Long, numbers: List<Long>, operators: List<String>): Long {
    val operatorsPlaces = numbers.size - 1
    val numberOfOperators = operators.size
    val allCombinations = numberOfOperators.toDouble().pow(operatorsPlaces.toDouble()).toInt()

    for (combination in 0 until allCombinations) {
        val currentOperators = (0 until operatorsPlaces).map { index ->
            when ((combination / numberOfOperators.toDouble().pow(index.toDouble()).toInt()) % numberOfOperators) {
                0 -> "+"
                1 -> "*"
                2 -> "||"
                else -> throw IllegalArgumentException("Unknown operator")
            }
        }
        if (calculate(numbers, currentOperators) == result) {
            return result
        }
    }
    return 0
}

fun calculate(numbers: List<Long>, operators: List<String>): Long {
    var result = numbers.first()
    for (i in operators.indices) {
        val next = numbers[i + 1]
        result = when (operators[i]) {
            "+" -> result + next
            "*" -> result * next
            "||" -> "$result$next".toLong()
            else -> throw IllegalArgumentException("Unknown operator")
        }
    }
    return result
}
