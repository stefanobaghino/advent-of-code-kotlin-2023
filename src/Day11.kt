import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun expand(input: List<String>): Array<CharArray> {
        val emptyColumnsPerRow =
            input.map { it.withIndex().filter { c -> c.value == '.' }.map { c -> c.index }.toSet() }
        val emptyColumns = emptyColumnsPerRow.reduce { l, r -> l.intersect(r) }.toList().sorted().reversed()
        val expandedHorizontally = input.map {
            val row = it.toMutableList()
            emptyColumns.forEach { c -> row.add(c, '.') }
            row.toCharArray()
        }
        val expandedVertically =
            expandedHorizontally.flatMap { if (it.all { c -> c == '.' }) listOf(it, it) else listOf(it) }
        return expandedVertically.toTypedArray()
    }

    fun galaxies(grid: Array<CharArray>): Set<Pair<Int, Int>> {
        val height = grid.size
        val width = grid[0].size
        val galaxies = mutableSetOf<Pair<Int, Int>>()
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (grid[y][x] == '#') {
                    galaxies.add(Pair(x, y))
                }
            }
        }
        return galaxies
    }

    fun shortestPath(galaxy: Pair<Int, Int>, galaxies: Set<Pair<Int, Int>>): Long {
        return galaxies.sumOf { abs(galaxy.first - it.first) + abs(galaxy.second - it.second) }.toLong()
    }

    fun delta(g1: Pair<Int, Int>, g2: Pair<Int, Int>, emptyRows: Set<Int>, emptyColumns: Set<Int>, factor: Int): Long {
        val xMax = max(g1.first, g2.first)
        val xMin = min(g1.first, g2.first)
        val yMax = max(g1.second, g2.second)
        val yMin = min(g1.second, g2.second)
        val expandedX = emptyColumns.count { it in IntRange(xMin, xMax) }.toLong()
        val expandedY = emptyRows.count { it in IntRange(yMin, yMax) }.toLong()
        val stableX = (xMax - xMin) - expandedX
        val stableY = (yMax - yMin) - expandedY

        val x = expandedX * factor + stableX
        val y = expandedY * factor + stableY

        return x + y
    }

    fun shortestPath(
        galaxy: Pair<Int, Int>,
        galaxies: Set<Pair<Int, Int>>,
        emptyRows: Set<Int>,
        emptyColumns: Set<Int>,
        factor: Int
    ): Long {
        return galaxies.sumOf { delta(galaxy, it, emptyRows, emptyColumns, factor) }
    }

    fun part1(input: List<String>): Long {
        val grid = expand(input)
        val galaxies = galaxies(grid)
        return galaxies.sumOf { shortestPath(it, galaxies) } / 2
    }

    fun part2(input: List<String>, factor: Int = 1_000_000): Long {
        val emptyRows = input.withIndex().filter { it.value.all { c -> c == '.' } }.map { it.index }.toSet()
        val emptyColumns = input.map { it.withIndex().filter { c -> c.value == '.' }.map { c -> c.index }.toSet() }
            .reduce { l, r -> l.intersect(r) }
        val galaxies = galaxies(input.map { it.toCharArray() }.toTypedArray())
        return galaxies.sumOf { shortestPath(it, galaxies, emptyRows, emptyColumns, factor) } / 2
    }

    val testInput = readInput("Day11_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 374L) { "expected: 374, got: $testPart1Result" }

    val testPart2Result1 = part2(testInput, factor = 10)
    check(testPart2Result1 == 1030L) { "expected: 1030, got: $testPart2Result1" }
    val testPart2Result2 = part2(testInput, factor = 100)
    check(testPart2Result2 == 8410L) { "expected: 8410, got: $testPart2Result2" }
    val testPart2Result3 = part2(testInput, factor = 2)
    check(testPart2Result3 == 374L) { "expected: 374, got: $testPart2Result3" }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
