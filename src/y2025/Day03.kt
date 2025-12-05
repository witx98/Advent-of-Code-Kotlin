package y2025

import println
import readInputLines


fun main() {
    val lines = readInputLines(2025, "day-03-input")
    val firstResult = firstPart(lines)
    val secondResult = secondPart(lines)

    firstResult.println()
    secondResult.println()
}

private fun secondPart(lines: List<String>): Long {
    return lines.sumOf { findBestBank(it) }
}

private fun findBestBank(bank: String): Long {
    val biggestCombination = StringBuilder()
    var currentIndex = 0
    for (i in 12 downTo 1) {
        val remainingDigits = i - 1
        val foundDigit = findBestDigit(bank, currentIndex, remainingDigits)

        biggestCombination.append(foundDigit.digit)
        currentIndex = foundDigit.position + 1
    }

    return biggestCombination.toString().toLong()
}

private fun findBestDigit(bank: String, currentIndex: Int, remainingDigits: Int): FoundDigit {
    for (i in 9 downTo 1) {
        val digit = i.digitToChar()
        val idx = bank.indexOf(digit, currentIndex)
        if (idx == -1) {
            continue
        }
        if (idx + remainingDigits >= bank.length) {
            continue
        }

        return FoundDigit(digit, idx)
    }
    throw IllegalStateException("No digit found")
}

private data class FoundDigit(val digit: Char, val position: Int)

private fun firstPart(lines: List<String>): Int {
    return lines.sumOf { maxJoltage(it) }
}

private fun maxJoltage(bank: String): Int {
    for (i in 99 downTo 11) {
        val tens = (i / 10).digitToChar()
        val ones = (i % 10).digitToChar()
        val tensIndex = bank.indexOf(tens)
        if (tensIndex == -1) {
            continue
        }
        val onesIndex = bank.indexOf(ones, tensIndex + 1)
        if (onesIndex == -1) {
            continue
        }
        return i
    }
    return 0
}