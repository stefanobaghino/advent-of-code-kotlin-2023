import java.lang.IllegalStateException

fun main() {

    val number = "\\d+".toRegex()

    fun part1(input: List<String>): Int {
        val times = number.findAll(input[0]).map { it.value.toInt() }.toList()
        val distances = number.findAll(input[1]).map { it.value.toInt() }.toList()
        val wins = IntArray(times.size) { 0 }
        for (i in times.indices) {
            var previous = 0
            val time = times[i]
            val distance = distances[i]
            for (press in 1..<time) {
                val covered = press * (time - press)
                if (covered > distance) {
                    wins[i]++
                } else if (covered < previous) {
                    break
                }
                previous = covered
            }
        }
        return wins.fold(1) { l, r -> l * r }
    }

    fun wins(i: Long, t: Long, d: Long): Boolean = i * (t - i) > d

    fun findApproximateBoundaries(t: Long, d: Long): Pair<Long, Long> {
        var samples = 10
        val wins = mutableListOf<Long>()
        while (true) {
            for (i in 0..<t step t / samples) {
                if (wins(i, t, d)) {
                    wins.add(i)
                }
            }
            if (((t / samples) < 10 || samples >= 1E9) && wins.size > 2) {
                return Pair(wins.first(), wins.last())
            }
            wins.clear()
            samples *= 10
        }
    }

    fun findFirstWin(l: Long, r: Long, t: Long, d: Long): Long {
        val gap = r - l
        if (gap < 10) {
            for (i in l..r) {
                if (wins(i, t, d)) {
                    return i
                }
            }
            throw IllegalStateException("not found in linear search")
        }
        val p = l + (gap / 2)
        return if (wins(p, t, d)) {
            findFirstWin(l, p, t, d)
        } else {
            findFirstWin(p, r, t, d)
        }
    }

    fun findLastWin(l: Long, r: Long, t: Long, d: Long): Long {
        val gap = r - l
        if (gap < 10) {
            for (i in l..r) {
                if (!wins(i, t, d)) {
                    return i
                }
            }
            throw IllegalStateException("not found in linear search")
        }
        val p = l + (gap / 2)
        return if (!wins(p, t, d)) {
            findLastWin(l, p, t, d)
        } else {
            findLastWin(p, r, t, d)
        }
    }

    fun part2(input: List<String>): Long {
        val time = number.findAll(input[0]).map { it.value }.joinToString("").toLong()
        val distance = number.findAll(input[1]).map { it.value }.joinToString("").toLong()
        val (left, right) = findApproximateBoundaries(time, distance)
        val first = findFirstWin(0, left, time, distance)
        val last = findLastWin(right, time - 1, time, distance)
        return last - first
    }

    val testInput = readInput("Day06_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 288) { "expected: 288, got: $testPart1Result" }

    val testPart2Result = part2(testInput)
    check(testPart2Result == 71503L) { "expected: 71503, got: $testPart2Result" }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
