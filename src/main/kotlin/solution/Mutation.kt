package solution

import org.uncommons.watchmaker.framework.EvolutionaryOperator
import java.util.*

class Mutation(
    private val problemSize: Int,
    private val mutationMethod: Method,
    private val prob: Double
) : EvolutionaryOperator<Solution> {
    override fun apply(population: List<Solution>, random: Random): List<Solution> =
        population.map {
            if (random.nextDouble() < prob) {
                mutationMethod.run(this, it, random)
            } else {
                it
            }
        }

    enum class Method(val run: (Mutation, Solution, Random) -> Solution) {
        SWAP(Mutation::swapMethod),
        SHUFFLE(Mutation::shuffleMethod),
        INSERT(Mutation::insertMethod),
        INVERSE(Mutation::inverseMethod),
    }

    private fun shuffleMethod(solution: Solution, random: Random): Solution =
        genTwoIndices(problemSize, random).let { (a, b) ->
            Solution(
                solution.data.subList(0, a) +
                        solution.data.subList(a, b).shuffled() +
                        solution.data.subList(b, problemSize)
            )
        }

    private fun inverseMethod(solution: Solution, random: Random) =
        genTwoIndices(problemSize, random).let { (a, b) ->
            Solution(
                solution.data.subList(0, a) +
                        solution.data.subList(a, b).reversed() +
                        solution.data.subList(b, problemSize)
            )
        }


    private fun swapMethod(solution: Solution, random: Random): Solution {
        val out = solution.data.toMutableList()
        val (a, b) = genTwoIndices(problemSize, random)
        Collections.swap(out, a, b)
        return Solution(out)
    }

    private fun insertMethod(solution: Solution, random: Random): Solution {
        val out = solution.data.toMutableList()
        val (a, b) = genTwoIndices(problemSize, random)
        val gen = out.removeAt(b)
        out.add(a, gen)
        return Solution(out)
    }
}