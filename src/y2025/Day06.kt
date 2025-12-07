package y2025

import println
import readInputLines


fun main() {
    val lines = readInputLines(2025, "day-06-input")
    val firstResult = firstPart(lines)
    val secondResult = secondPart(lines)

    firstResult.println()
    secondResult.println()
}


private fun secondPart(input: List<String>): Long {
    val onlyNumbers = input.dropLast(1)
    val operators = input.takeLast(1)[0].split(Regex("\\s+"))
    val numbers = parseNumbersVertically(onlyNumbers)

    return numbers.zip(operators).map { (numbers, operation) -> Equation(operation, numbers) }.sumOf { it.solve() }
}

private fun parseNumbersVertically(onlyNumbers: List<String>): List<List<Long>> {
    val transposed = transpose(onlyNumbers)
    val numbers = mutableListOf(mutableListOf<Long>())
    for (line in transposed) {
        if (line.isEmpty()) {
            numbers += mutableListOf<Long>()
        } else {
            numbers.last() += line.toLong()
        }
    }
    return numbers
}

private fun transpose(onlyNumbers: List<String>): List<String> {
    val maxLineLength = onlyNumbers.maxOf { it.length }
    val result = MutableList(maxLineLength) { "" }

    for (row in onlyNumbers) {
        for (col in row.indices) {
            result[col] += row[col]
        }
    }
    return result.map { it.trim() }
}

private fun firstPart(lines: List<String>): Long {
    val input = lines.map { parseLine(it) }.toList()
    return parseEquations(input).map { it.solve() }.sumOf { it }
}

private fun parseLine(line: String): List<String> {
    return line.trim().split(Regex("\\s+"))
}

private fun parseEquations(input: List<List<String>>): List<Equation> {
    val equations = mutableListOf<Equation>()
    val onlyNumbers = input.dropLast(1)
    val operators = input.takeLast(1)[0]
    for (row in 0..onlyNumbers[0].lastIndex) {
        val operation = operators[row]
        val numbers = mutableListOf<Long>()
        for (column in 0..onlyNumbers.lastIndex) {
            numbers.add(onlyNumbers[column][row].toLong())
        }
        equations.add(Equation(operation, numbers))
    }

    return equations
}

private data class Equation(val operation: String, val numbers: List<Long>) {

    fun solve(): Long {
        return when (operation) {
            "+" -> numbers.sum()
            "*" -> numbers.reduce { acc, l -> acc * l }
            else -> throw IllegalArgumentException("Unknown operation")
        }
    }
}