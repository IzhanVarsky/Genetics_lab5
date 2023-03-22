package solution

import org.uncommons.watchmaker.framework.FitnessEvaluator

class FitnessFunction : FitnessEvaluator<Solution> {
    var cnt = 0
    override fun getFitness(solution: Solution, list: List<Solution>): Double {
        cnt += 1
        val permutation = solution.data
        var sum = 0.0
        for ((x1, y1) in permutation.withIndex()) {
            var x2 = x1 + 1
            while (x2 < permutation.size) {
                val y2 = permutation[x2]
                if (x1 - y1 == x2 - y2 || x1 + y1 == x2 + y2 || y1 == y2) {
                    sum += 1
                }
                x2++
            }
        }
        return sum
    }

    override fun isNatural(): Boolean {
        return false
    }

}