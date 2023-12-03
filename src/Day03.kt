import java.util.concurrent.Callable

data class Symbol(val column: Int)

data class Number(val range: IntRange, val value: Int) {
    fun touches(s: Symbol): Boolean = range.contains(s.column - 1) || range.contains(s.column + 1)
}

data class Row(
    val symbols: Sequence<Symbol>,
    val numbers: Sequence<Number>,
    val getBefore: Callable<Row>,
    val getAfter: Callable<Row>
) {
    fun valid(): Int {
        val before = getBefore.call()?.symbols ?: emptySequence()
        val after = getAfter.call()?.symbols ?: emptySequence()
        return numbers.filter { n ->
            symbols.any { n.touches(it) } || before.any { n.touches(it) } || after.any {
                n.touches(
                    it
                )
            }
        }
            .sumOf { it.value }
    }

    fun validGears(): Int {
        val before = getBefore.call()?.numbers ?: emptySequence()
        val after = getAfter.call()?.numbers ?: emptySequence()
        return symbols.map { gear -> (numbers + before + after).filter { it.touches(gear) } }
            .filter { it.toList().size == 2 }
            .sumOf { it.fold(1) { acc: Int, num -> acc * num.value } }
    }
}

fun main() {

    val gears = "\\*".toRegex()
    val symbols = "[^\\d.]".toRegex()
    val numbers = "\\d+".toRegex()

    fun part1(input: List<String>): Int {
        val length = input.size
        val rows = arrayOfNulls<Row>(length)
        input.mapIndexed { idx, line ->
            rows[idx] =
                Row(
                    symbols.findAll(line).map { Symbol(it.range.first) },
                    numbers.findAll(line).map { Number(it.range, it.value.toInt()) },
                    { if (idx > 0) rows[idx - 1] else null },
                    { if (idx < (length - 1)) rows[idx + 1] else null }
                )
        }
        return rows.sumOf { it!!.valid() }
    }

    fun part2(input: List<String>): Int {
        val length = input.size
        val rows = arrayOfNulls<Row>(length)
        input.mapIndexed { idx, line ->
            rows[idx] =
                Row(
                    gears.findAll(line).map { Symbol(it.range.first) },
                    numbers.findAll(line).map { Number(it.range, it.value.toInt()) },
                    { if (idx > 0) rows[idx - 1] else null },
                    { if (idx < (length - 1)) rows[idx + 1] else null }
                )
        }
        return rows.sumOf { it!!.validGears() }
    }

    val testInput = readInput("Day03_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 4361) { "expected: 4361, got: $testPart1Result" }

    val testPart2Result = part2(testInput)
    check(testPart2Result == 467835) { "expected: 467835, got: $testPart2Result" }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
