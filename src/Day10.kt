import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

enum class Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN,
}

// requires bumping the stack, -Xss512m should be more than enough
fun main() {

    fun start(grid: Array<CharArray>, height: Int, width: Int): Pair<Int, Int> {
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (grid[y][x] == 'S') {
                    return Pair(x, y)
                }
            }
        }
        throw IllegalArgumentException("no starting point found")
    }

    fun direction(
        c: Char,
        d: Direction
    ): Direction {
        return when (d) {
            Direction.LEFT ->
                when (c) {
                    '-' -> Direction.LEFT
                    'L' -> Direction.UP
                    'F' -> Direction.DOWN
                    else -> throw IllegalStateException("no direction")
                }

            Direction.RIGHT ->
                when (c) {
                    '-' -> Direction.RIGHT
                    'J' -> Direction.UP
                    '7' -> Direction.DOWN
                    else -> throw IllegalStateException("no direction")
                }

            Direction.UP ->
                when (c) {
                    '|' -> Direction.UP
                    'F' -> Direction.RIGHT
                    '7' -> Direction.LEFT
                    else -> throw IllegalStateException("no direction")
                }

            Direction.DOWN ->
                when (c) {
                    '|' -> Direction.DOWN
                    'L' -> Direction.RIGHT
                    'J' -> Direction.LEFT
                    else -> throw IllegalStateException("no direction")
                }
        }
    }

    fun directions(
        x: Int,
        y: Int,
        grid: Array<CharArray>,
        height: Int,
        width: Int,
    ): List<Direction> {
        val directions = mutableListOf<Direction>()
        if (x > 0 && grid[y][x - 1] in setOf('-', 'L', 'F', 'S')) {
            directions.add(Direction.LEFT)
        }
        if ((x + 1) < width && grid[y][x + 1] in setOf('-', 'J', '7', 'S')) {
            directions.add(Direction.RIGHT)
        }
        if (y > 0 && grid[y - 1][x] in setOf('|', '7', 'F', 'S')) {
            directions.add(Direction.UP)
        }
        if ((y + 1) < height && grid[y + 1][x] in setOf('|', 'L', 'J', 'S')) {
            directions.add(Direction.DOWN)
        }
        return directions
    }

    fun part1(input: List<String>): Long {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val width = grid[0].size
        val height = grid.size
        val (sx, sy) = start(grid, height, width)
        val directions = directions(sx, sy, grid, height, width)
        // for each direction, try to find a loop
        for (direction in directions) {
            var x = sx
            var y = sy
            var d = direction
            var l = 0
            // The puzzle input only has two direction available, meaning that they
            // _have_ to be the extremities of the loop, assuming a loop is found
            while (true) {
                when (d) {
                    Direction.LEFT -> x -= 1
                    Direction.RIGHT -> x += 1
                    Direction.UP -> y -= 1
                    Direction.DOWN -> y += 1
                }
                l += 1
                if (x == sx && y == sy) {
                    return l.toLong() / 2
                }
                d = direction(grid[y][x], d)
            }
        }
        // if a loop is found, divide its length by two
        throw IllegalArgumentException("no loop found")
    }

    fun map(loop: Map<Pair<Int, Int>, Char>, width: Int, height: Int): Array<CharArray> {
        val out = Array(3 * height) { CharArray(3 * width) { ' ' } }
        for (y in out.indices) {
            for (x in out[y].indices) {
                if ((x-1)%3 == 0 && (y-1)%3 == 0) {
                    out[y][x] = '.'
                }
            }
        }
        for (y in 0..<height) {
            val yOut = y * 3
            for (x in 0..<width) {
                val xOut = x * 3
                val tile = loop[Pair(x, y)]
                when (tile) {
                    null -> {

                    }
                    '|' -> {
                        out[yOut][xOut+1] = '#'
                        out[yOut+1][xOut+1] = '#'
                        out[yOut+2][xOut+1] = '#'
                    }

                    '-' -> {
                        out[yOut+1][xOut] = '#'
                        out[yOut+1][xOut+1] = '#'
                        out[yOut+1][xOut+2] = '#'
                    }

                    'F' -> {
                        out[yOut+1][xOut+1] = '#'
                        out[yOut+1][xOut+2] = '#'
                        out[yOut+2][xOut+1] = '#'
                    }

                    'J' -> {
                        out[yOut+1][xOut+1] = '#'
                        out[yOut+1][xOut] = '#'
                        out[yOut][xOut+1] = '#'
                    }
                    '7' -> {
                        out[yOut+1][xOut+1] = '#'
                        out[yOut+1][xOut] = '#'
                        out[yOut+2][xOut+1] = '#'
                    }

                    'L' -> {
                        out[yOut+1][xOut+1] = '#'
                        out[yOut+1][xOut+2] = '#'
                        out[yOut][xOut+1] = '#'
                    }

                    else -> throw IllegalArgumentException("unknown tile")
                }
            }
        }
        return out
    }

    fun refine(map: Array<CharArray>, x: Int, y: Int, visited: MutableSet<Pair<Int, Int>>) {
        if (Pair(x, y) in visited || x < 0 || y < 0 || y >= map.size || x >= map[y].size || map[y][x] == '#') {
            return
        }
        map[y][x] = ' '
        visited.add(Pair(x, y))
        refine(map, x+1, y, visited)
        refine(map, x, y+1, visited)
        refine(map, x-1, y, visited)
        refine(map, x, y-1, visited)
    }

    fun part2(input: List<String>): Long {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val width = grid[0].size
        val height = grid.size
        val (sx, sy) = start(grid, height, width)
        val directions = directions(sx, sy, grid, height, width)
        if (directions.contains(Direction.LEFT) && directions.contains(Direction.RIGHT)) {
            grid[sy][sx] = '-';
        } else if (directions.contains(Direction.UP) && directions.contains(Direction.DOWN)) {
            grid[sy][sx] = '|';
        } else if (directions.contains(Direction.LEFT) && directions.contains(Direction.DOWN)) {
            grid[sy][sx] = '7';
        } else if (directions.contains(Direction.RIGHT) && directions.contains(Direction.DOWN)) {
            grid[sy][sx] = 'F';
        } else if (directions.contains(Direction.UP) && directions.contains(Direction.LEFT)) {
            grid[sy][sx] = 'J';
        } else if (directions.contains(Direction.UP) && directions.contains(Direction.RIGHT)) {
            grid[sy][sx] = 'L';
        } else {
            throw IllegalStateException("more than two entry points")
        }
        // for each direction, try to find a loop
        for (direction in directions) {
            var x = sx
            var y = sy
            var d = direction
            var l = 0
            val loop = mutableMapOf(Pair(x, y) to 'S')
            // The puzzle input only has two direction available, meaning that they
            // _have_ to be the extremities of the loop, assuming a loop is found
            while (true) {
                when (d) {
                    Direction.LEFT -> x -= 1
                    Direction.RIGHT -> x += 1
                    Direction.UP -> y -= 1
                    Direction.DOWN -> y += 1
                }
                loop[Pair(x, y)] = grid[y][x]
                l += 1
                if (x == sx && y == sy) {
                    val m = map(loop, width, height)
                    refine(m, 0, 0, mutableSetOf())
                    return m.sumOf { it.count { c -> c == '.' }}.toLong()
                }
                d = direction(grid[y][x], d)
            }
        }
        // if a loop is found, divide its length by two
        throw IllegalArgumentException("no loop found")
    }

    val testInput1 = readInput("Day10_test1")
    val testInput2 = readInput("Day10_test2")

    val testPart1Result1 = part1(testInput1)
    check(testPart1Result1 == 4L) { "expected: 4, got: $testPart1Result1" }

    val testPart1Result2 = part1(testInput2)
    check(testPart1Result2 == 8L) { "expected: 8, got: $testPart1Result2" }

    val testInput3 = readInput("Day10_test3")
    val testInput4 = readInput("Day10_test4")
    val testInput5 = readInput("Day10_test5")
    val testInput6 = readInput("Day10_test6")

    val testPart2Result1 = part2(testInput1)
    check(testPart2Result1 == 1L) { "expected: 1, got: $testPart2Result1" }
    val testPart2Result2 = part2(testInput2)
    check(testPart2Result2 == 1L) { "expected: 1, got: $testPart2Result2" }
    val testPart2Result3 = part2(testInput3)
    check(testPart2Result3 == 4L) { "expected: 4, got: $testPart2Result3" }
    val testPart2Result4 = part2(testInput4)
    check(testPart2Result4 == 4L) { "expected: 4, got: $testPart2Result4" }
    val testPart2Result5 = part2(testInput5)
    check(testPart2Result5 == 8L) { "expected: 8, got: $testPart2Result5" }
    val testPart2Result6 = part2(testInput6)
    check(testPart2Result6 == 10L) { "expected: 10, got: $testPart2Result6" }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
