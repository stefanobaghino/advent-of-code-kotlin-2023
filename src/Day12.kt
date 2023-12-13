fun main() {

    val damaged = "#+".toRegex()

    fun checksOut(report: String, check: List<Int>): Boolean {
        for (match in damaged.findAll(report).withIndex()) {
            if (match.index >= check.size || match.value.value.length != check[match.index]) {
                return false
            }
        }
        return true
    }

    fun arrangements(report: String, check: List<Int>): Long {
        val unknown = report.indexOf('?')
        return if (unknown < 0) {
            if (checksOut(report, check) && damaged.findAll(report).count() == check.size) 1L else 0L
        } else {
            val known = report.take(unknown).dropLastWhile { it == '#' }
            if (checksOut(known, check)) {
                arrangements(report.replaceFirst('?', '.'), check) + arrangements(report.replaceFirst('?', '#'), check)
            } else {
                0L
            }
        }
    }

    fun unfold(n: Int, s: String, separator: String): String =
        List(n) { s }.joinToString(separator)

    fun arrangements(line: String, factor: Int): Long {
//        println(line)
        val reportAndCheck = line.split(" ")
        val report = unfold(factor, reportAndCheck[0], "?")
        val check = unfold(factor, reportAndCheck[1], ",")
        return arrangements(report, check.split(",").map { it.toInt() })
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { arrangements(it, factor = 1) }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { arrangements(it, factor = 5) }
    }

    val testInput = readInput("Day12_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 21L) { "expected: 21, got: $testPart1Result" }

    val testPart2Result = part2(testInput)
    check(testPart2Result == 525152L) { "expected: 525152, got: $testPart2Result" }

    val input = readInput("Day12")
    part1(input).println()
//    part2(input).println()
}
