import kotlin.math.min

interface AlmanacMapEntry {
    fun get(i: Long): Long

    companion object {
        fun of(ns: List<Long>): AlmanacMapEntry {
            require(ns.size == 3)
            return ExplicitAlmanacMapEntry(sourceFrom = ns[1], destinationFrom = ns[0], rangeSize = ns[2])
        }
    }
}

data class ExplicitAlmanacMapEntry(val sourceFrom: Long, val destinationFrom: Long, val rangeSize: Long) :
    AlmanacMapEntry {
    private val sourceTo = sourceFrom + rangeSize
    override fun get(i: Long): Long =
        if (i in sourceFrom..<sourceTo) {
            destinationFrom + (i - sourceFrom)
        } else {
            -1
        }
}

object FallbackAlmanacMapEntry : AlmanacMapEntry {
    override fun get(i: Long): Long = i
}

data class AlmanacMap(val entries: List<AlmanacMapEntry>) {
    fun get(i: Long): Long =
        (entries.find { it.get(i) >= 0 } ?: FallbackAlmanacMapEntry).get(i)
}

fun main() {

    val regex = "[:\\d]+".toRegex()

    fun part1(input: List<String>): Long {
        val raw = input.flatMap { line -> regex.findAll(line).map { it.value } }
        val separators = raw.withIndex().filter { s -> s.value == ":" }.map { s -> s.index } + raw.size
        val parsed = separators.windowed(2)
            .map { range -> raw.slice(IntRange(range.first() + 1, range.last() - 1)).map { it.toLong() } }
        val seeds = parsed[0]
        val maps =
            parsed.drop(1).map { parsedMap -> AlmanacMap(parsedMap.windowed(3, 3).map { AlmanacMapEntry.of(it) }) }
        var lowest = Long.MAX_VALUE
        for (seed in seeds) {
            var source = seed
            for (map in maps) {
                source = map.get(source)
            }
            lowest = min(lowest, source)
        }
        return lowest
    }

    fun part2(input: List<String>): Long {
        val raw = input.flatMap { line -> regex.findAll(line).map { it.value } }
        val separators = raw.withIndex().filter { s -> s.value == ":" }.map { s -> s.index } + raw.size
        val parsed = separators.windowed(2)
            .map { range -> raw.slice(IntRange(range.first() + 1, range.last() - 1)).map { it.toLong() } }
        val seedRanges = parsed[0].windowed(2, 2).map { LongRange(it[0], it[0] + it[1] - 1) }
        val maps =
            parsed.drop(1).map { parsedMap -> AlmanacMap(parsedMap.windowed(3, 3).map { AlmanacMapEntry.of(it) }) }
        var lowest = Long.MAX_VALUE
        for (seedRange in seedRanges) {
            for (seed in seedRange) {
                var source = seed
                for (map in maps) {
                    source = map.get(source)
                }
                lowest = min(lowest, source)
            }
        }
        return lowest
    }

    val testInput = readInput("Day05_test")

    val testPart1Result = part1(testInput)
    check(testPart1Result == 35L) { "expected: 35, got: $testPart1Result" }

    val testPart2Result = part2(testInput)
    check(testPart2Result == 46L) { "expected: 46, got: $testPart2Result" }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
