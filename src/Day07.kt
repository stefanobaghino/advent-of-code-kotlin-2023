data class CamelCards(val hand: String, val bid: Long, val joker: Boolean = false) {
    private val actual: String = if (joker && hand.contains('J')) {
        "AKQT98765432".map { hand.replace('J', it) }.maxBy { CamelCards(it, bid).value }
    } else {
        hand
    }
    private val counts = actual.groupBy { it }.mapValues { it.value.size }
    private val type: String =
        if (counts.size == 1) {
            "F"
        } else if (counts.values.any { it == 4 }) {
            "E"
        } else if (counts.size == 2) {
            "D"
        } else if (counts.values.any { it == 3 }) {
            "C"
        } else if (counts.values.count { it == 2 } == 2) {
            "B"
        } else if (counts.values.any { it == 2 }) {
            "A"
        } else {
            "9"
        }

    private fun toKey(c: Char): Char =
        when (c) {
            'A' -> 'E'
            'K' -> 'D'
            'Q' -> 'C'
            'J' -> if (joker) '1' else 'B'
            'T' -> 'A'
            else -> c
        }

    val value: Int = hand.map { toKey(it) }.joinToString(separator = "", prefix = type).toInt(radix = 16)
}

fun main() {

    fun part1(input: List<String>): Long {
        return input.map {
            val fields = it.split(" ")
            CamelCards(fields[0], fields[1].toLong())
        }.sortedBy { it.value }.withIndex().sumOf { it.value.bid * (it.index + 1) }
    }

    fun part2(input: List<String>): Long {
        return input.map {
            val fields = it.split(" ")
            CamelCards(fields[0], fields[1].toLong(), joker = true)
        }.sortedBy { it.value }.withIndex().sumOf { it.value.bid * (it.index + 1) }
    }

    val testInput = readInput("Day07_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 6440L) { "expected: 6440, got: $testPart1Result" }

    val testPart2Result = part2(testInput)
    check(testPart2Result == 5905L) { "expected: 5905, got: $testPart2Result" }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
