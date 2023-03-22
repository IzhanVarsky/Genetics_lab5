package solution

import java.util.Random

fun genTwoIndices(problemSize: Int, random: Random): Pair<Int, Int> {
    var a = random.nextInt(problemSize)
    var b = random.nextInt(problemSize)
    if (a > b) {
        val t = a
        a = b
        b = t
    }
    return a to b
}