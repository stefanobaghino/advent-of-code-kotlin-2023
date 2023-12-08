import java.lang.IllegalArgumentException
import kotlin.math.max

data class State(var position: String, var steps: Long = 0, var done: Boolean = false)

fun main() {

    val regex = "(\\w+) = \\((\\w+), (\\w+)\\)".toRegex()

    fun part1(input: List<String>): Int {
        val directions = input[0]
        val map = input.drop(2).associate { line ->
            val match = regex.find(line)!!
            Pair(match.groupValues[1], arrayOf(match.groupValues[2], match.groupValues[3]))
        }
        var position = "AAA"
        var steps = 0
        while (true) {
            for (direction in directions) {
                steps += 1
                position = when (direction) {
                    'L' -> map[position]!![0]
                    'R' -> map[position]!![1]
                    else -> throw IllegalArgumentException("$direction is neither L not R")
                }
                if (position == "ZZZ") return steps
            }
        }
    }

    fun lcm(a: Long, b: Long): Long {
        val larger = max(a, b)
        val max = a * b
        var lcm = larger
        while (lcm <= max) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return max
    }

    fun lcm(ns: List<Long>): Long {
        return ns.fold(1L) { a, b -> lcm(a, b) }
    }

    fun part2(input: List<String>): Long {
        val directions = input[0]
        val map = input.drop(2).associate { line ->
            val match = regex.find(line)!!
            Pair(match.groupValues[1], arrayOf(match.groupValues[2], match.groupValues[3]))
        }
        val states = map.filterKeys { it.endsWith('A') }.keys.map { State(it) }.toTypedArray()
        while (true) {
            for (direction in directions) {
                for (i in states.indices) {
                    if (states[i].done) continue
                    states[i].steps += 1
                    states[i].position = when (direction) {
                        'L' -> map[states[i].position]!![0]
                        'R' -> map[states[i].position]!![1]
                        else -> throw IllegalArgumentException("$direction is neither L not R")
                    }
                    if (states[i].position.endsWith('Z')) {
                        states[i].done = true
                    }
                }
                if (states.all { it.done }) return lcm(states.map { it.steps })
            }
        }
    }

    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")

    val testPart1Result1 = part1(testInput1)
    check(testPart1Result1 == 2) { "expected: 2, got: $testPart1Result1" }

    val testPart1Result2 = part1(testInput2)
    check(testPart1Result2 == 6) { "expected: 6, got: $testPart1Result2" }

    val testInput3 = readInput("Day08_test3")
    val testPart2Result = part2(testInput3)
    check(testPart2Result == 6L) { "expected: 6, got: $testPart2Result" }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
