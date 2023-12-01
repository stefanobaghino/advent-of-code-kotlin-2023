fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val ns = line.filter { it.isDigit() }
            ns.first().digitToInt() * 10 + ns.last().digitToInt()
        }
    }

    val values = mapOf(
        "one" to 1,
        "eno" to 1,
        "1" to 1,
        "two" to 2,
        "owt" to 2,
        "2" to 2,
        "three" to 3,
        "eerht" to 3,
        "3" to 3,
        "four" to 4,
        "ruof" to 4,
        "4" to 4,
        "five" to 5,
        "evif" to 5,
        "5" to 5,
        "six" to 6,
        "xis" to 6,
        "6" to 6,
        "seven" to 7,
        "neves" to 7,
        "7" to 7,
        "eight" to 8,
        "thgie" to 8,
        "8" to 8,
        "nine" to 9,
        "enin" to 9,
        "9" to 9
    )
    val straight = "one|two|three|four|five|six|seven|eight|nine|\\d".toRegex()
    val reverse = "eno|owt|eerht|ruof|evif|xis|neves|thgie|enin|\\d".toRegex()

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val n = values[straight.find(line)!!.value]!!.toInt()
            val m = values[reverse.find(line.reversed())!!.value]!!.toInt()
            n * 10 + m
        }
    }

    val testPart1Input = readInput("Day01Part1_test")
    val testPart1Result = part1(testPart1Input)
    check(testPart1Result == 142) { "expected: 142, got: $testPart1Result" }

    val testPart2Input = readInput("Day01Part2_test")
    val testPart2Result = part2(testPart2Input)
    check(testPart2Result == 281) { "expected: 281, got: $testPart2Result" }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
