package solution

import org.uncommons.maths.random.Probability
import org.uncommons.watchmaker.framework.operators.AbstractCrossover
import java.util.Random

class Crossover(private val problemSize: Int, prob: Double) :
    AbstractCrossover<Solution>(1, Probability(prob)) {
    public override fun mate(p1: Solution, p2: Solution, i: Int, random: Random): List<Solution> =
        method1(p1, p2, random)

    private fun method1(p1: Solution, p2: Solution, random: Random): List<Solution> {
        val (a, b) = genTwoIndices(problemSize, random)
        val subListLeft1 = mutableListOf<Int>()
        val subListMiddle1 = p1.data.subList(a, b).toMutableList()
        val subListRight1 = mutableListOf<Int>()
        val subListMiddle2 = mutableListOf<Int>()

        for (range in listOf(
            b until problemSize,
            0 until b
        )) {
            for (ind in range) {
                val curPointNum = p2.data[ind]
                if (curPointNum !in subListMiddle1) {
                    if (subListRight1.size < problemSize - b) {
                        subListRight1 += curPointNum
                    } else {
                        subListLeft1 += curPointNum
                    }
                } else {
                    subListMiddle2 += curPointNum
                }
            }
        }
        return listOf(
            Solution(subListLeft1 + subListMiddle1 + subListRight1),
            Solution(
                p1.data.subList(0, a).toMutableList() +
                        subListMiddle2 +
                        p1.data.subList(b, problemSize).toMutableList()
            ),
        )
    }

    private fun getOneOffspring(p1: Solution, p2: Solution, random: Random): Solution {
        val (a, b) = genTwoIndices(problemSize, random)
        val subListLeft1 = mutableListOf<Int>()
        val subListMiddle1 = p1.data.subList(a, b).toMutableList()
        val subListRight1 = mutableListOf<Int>()

        for (range in listOf(
            b until problemSize,
            0 until b
        )) {
            for (ind in range) {
                val curPointNum = p2.data[ind]
                if (curPointNum !in subListMiddle1) {
                    if (subListRight1.size < problemSize - b) {
                        subListRight1 += curPointNum
                    } else {
                        subListLeft1 += curPointNum
                    }
                }
            }
        }
        return Solution(subListLeft1 + subListMiddle1 + subListRight1)
    }

    private fun method2(p1: Solution, p2: Solution, random: Random): List<Solution> =
        listOf(
            getOneOffspring(p1, p2, random),
            getOneOffspring(p2, p1, random),
//            oneChildMainInfo(p2, p1, random),
        )

    private fun oneChildMainInfo(p1: Solution, p2: Solution, random: Random): Solution {
        val outChild = MutableList(problemSize) { -1 }
        val notSameValues = mutableSetOf<Int>()
        for (ind in p1.data.indices) {
            val v1 = p1.data[ind]
            val v2 = p2.data[ind]
            if (v1 == v2) {
                outChild[ind] = v1
            } else {
                notSameValues += v1
                notSameValues += v2
            }
        }
        val shuffledNotSameValues = notSameValues.shuffled(random).toMutableList()
        outChild.forEachIndexed { index, v ->
            if (v == -1) outChild[index] = shuffledNotSameValues.removeLast()
        }
        return Solution(outChild)
    }
}