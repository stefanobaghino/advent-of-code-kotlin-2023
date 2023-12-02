data class Hand(var red: Int, var green: Int, var blue: Int) {
    fun canBeDrawnFrom(that: Hand): Boolean = this.red <= that.red && this.green <= that.green && this.blue <= that.blue

    fun power(): Int = red * green * blue

    companion object {
        fun empty(): Hand {
            return Hand(0, 0, 0)
        }
    }
}

data class Game(val id: Int, val hands: List<Hand>, val min: Hand)

fun main() {

    // only 12 red cubes, 13 green cubes, and 14 blue cubes
    val max = Hand(red = 12, green = 13, blue = 14)

    // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    val cubes = "(\\d+)\\s+(red|green|blue)".toRegex()

    fun parse(line: String): Game {
        val gameAndHands = line.split(":")
        val id = "Game (\\d+)".toRegex().find(gameAndHands[0])!!.groupValues[1].toInt()
        val hands = mutableListOf<Hand>()
        val min = Hand.empty()
        for (handString in gameAndHands[1].split(";")) {
            val hand = Hand.empty()
            for (cubesString in handString.split(",")) {
                val matches = cubes.findAll(cubesString)
                for (match in matches) {
                    val result = match.groupValues
                    when (result[2]) {
                        "red" -> hand.red = result[1].toInt()
                        "green" -> hand.green = result[1].toInt()
                        "blue" -> hand.blue = result[1].toInt()
                    }
                    if (hand.red > min.red) {
                        min.red = hand.red
                    }
                    if (hand.green > min.green) {
                        min.green = hand.green
                    }
                    if (hand.blue > min.blue) {
                        min.blue = hand.blue
                    }
                }
            }
            hands.add(hand)
        }
        return Game(id, hands, min)
    }

    fun part1(input: List<String>): Int {
        return input.map { parse(it) }.filter { game -> game.hands.all { it.canBeDrawnFrom(max) } }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return input.map { parse(it) }.sumOf { it.min.power() }
    }

    val testInput = readInput("Day02_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 8) { "expected: 8, got: $testPart1Result" }

    val testPart2Result = part2(testInput)
    check(testPart2Result == 2286) { "expected: 2286, got: $testPart2Result" }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
