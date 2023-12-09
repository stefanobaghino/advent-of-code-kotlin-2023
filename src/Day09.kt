fun main() {

    fun diff(ns: List<Long>): List<Long> {
        return ns.windowed(2).map { it[1] - it[0] }
    }

    fun extrapolate(ns: List<Long>): Long {
        val diff = diff(ns)
        return if (diff.all { it == 0L }) {
            ns.first()
        } else {
            ns.last() + extrapolate(diff)
        }
    }

    fun extrapolateLeft(ns: List<Long>): Long {
        val diff = diff(ns)
        return if (diff.all { it == 0L }) {
            ns.first()
        } else {
            ns.first() - extrapolateLeft(diff)
        }
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line -> extrapolate(line.split(" ").map { it.toLong() }) }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line -> extrapolateLeft(line.split(" ").map { it.toLong() }) }
    }

    val testInput = readInput("Day09_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 114L) { "expected: 114, got: $testPart1Result" }

    val testPart2Result = part2(testInput)
    check(testPart2Result == 2L) { "expected: 2, got: $testPart2Result" }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
