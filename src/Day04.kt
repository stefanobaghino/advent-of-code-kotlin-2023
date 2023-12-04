import kotlin.math.pow

data class Card(var copies: Int, val winning: Set<Int>, val numbers: Set<Int>) {
    fun wins(): Int {
        return winning.intersect(numbers).size
    }

    fun score(): Int {
        val wins = wins()
        if (wins == 0) {
            return 0
        }
        return 2.0.pow(wins - 1.0).toInt()
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val allNumbers = line.split(":")[1].split("|")
            Card(
                copies = 1,
                allNumbers[0].split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet(),
                allNumbers[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet(),
            ).score()
        }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { line ->
            val allNumbers = line.split(":")[1].split("|")
            Card(
                copies = 1,
                allNumbers[0].split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet(),
                allNumbers[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet(),
            )
        }.toTypedArray()
        for ((index, card) in cards.withIndex()) {
            cards.drop(index + 1).take(card.wins()).forEach { it.copies += card.copies }
        }
        return cards.sumOf { it.copies }
    }

    val testInput = readInput("Day04_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 13) { "expected: 13, got: $testPart1Result" }

    val testPart2Result = part2(testInput)
    check(testPart2Result == 30) { "expected: 30, got: $testPart2Result" }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
