package y2025

import readInputLines


fun main() {
    val machines = readInputLines(2025, "day-10-input").map(::parseLine)
    val firstResult = firstPart(machines)
//    val secondResult = secondPart(lines)

//    firstResult.println()
//    secondResult.println()
}


private fun firstPart(machines: List<Machine>): Int {

    return 0
}

private fun secondPart(lines: List<String>): Int {

    return 0
}

private fun parseLine(line: String): Machine {
    val switches = switches(line)
    val buttons = buttons(line)
    val joltages = joltages(line)


    println("Switches $switches")
    println("Buttons $buttons")
    println("Joltages $joltages")

    return Machine(switches, buttons, joltages)
}

private data class Machine(val switches: List<Switch>, val  buttons: List<Button>, val  joltages: List<Int>)

fun joltages(input: String): List<Int> {
    val joltagesStartIndex = input.indexOf("{")
    val joltagesEndIndex = input.indexOf("}")
    val joltagesSubstring = input.substring(joltagesStartIndex + 1, joltagesEndIndex)

    return joltagesSubstring.split(",").map { it.toInt() }
}

private fun buttons(input: String): List<Button> {
    val buttonsStartIndex = input.indexOf("(")
    val buttonsEndIndex = input.lastIndexOf(")")
    val buttonsSubstring = input.substring(buttonsStartIndex, buttonsEndIndex + 1)

    val splittedButtons = buttonsSubstring.split(" ")
    val buttons = splittedButtons.map { part ->
        val switches = part.removePrefix("(")
            .removeSuffix(")")
            .split(",")
            .map { it.toInt() }
            .toList()
        Button(switches)
    }

    return buttons
}

private fun switches(input: String): List<Switch> {
    val switchesStartIndex = input.indexOf("[")
    val switchesEndIndex = input.indexOf("]")
    val switchesSubstring = input.substring(switchesStartIndex + 1, switchesEndIndex).trim()
    return switchesSubstring.split("").filter { it.isNotBlank() }.map {
        when (it) {
            "." -> Switch.OFF
            "#" -> Switch.ON
            else -> throw IllegalArgumentException("Unknown switch: $it")
        }
    }
}

private data class Button(val switches: List<Int>)

private enum class Switch {
    ON,
    OFF;

    fun switch(): Switch = when (this) {
        ON -> OFF
        OFF -> ON
    }
}
