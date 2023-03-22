package solution

import org.uncommons.watchmaker.framework.EvolutionaryOperator
import org.uncommons.watchmaker.framework.SteadyStateEvolutionEngine
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline
import org.uncommons.watchmaker.framework.selection.RankSelection
import org.uncommons.watchmaker.framework.termination.GenerationCount
import java.util.*

object RunAlgo {
    private var bestValue = Double.MAX_VALUE
    private var bestIter = -1
    private var bestSolution = listOf<Int>()
    private var bestFitnessFunctionCount = -1

    @JvmStatic
    fun main(args: Array<String>) {
        val problemSize = 700

        val populationSize = 1500 // size of population
        val generations = 3000 // number of generations

        val random = Random() // random

        val factory = Factory(problemSize) // generation of solutions

        val operators = listOf<EvolutionaryOperator<Solution>>(
            Crossover(problemSize, 0.1),
            Mutation(problemSize, Mutation.Method.SWAP, 0.5)
        )
        val pipeline = EvolutionPipeline(operators)

        val selection = RankSelection() // Selection operator

        val evaluator = FitnessFunction() // Fitness function

        val algorithm = SteadyStateEvolutionEngine(
            factory, pipeline, evaluator, selection,
            populationSize, false, random
        )
        algorithm.addEvolutionObserver { populationData ->
            val bestFit = populationData.bestCandidateFitness
            println("Generation " + populationData.generationNumber + ": " + bestFit)
            val best = populationData.bestCandidate as Solution
            println("\tBest solution = ${best.data}")
            if (bestFit < bestValue) {
                bestValue = bestFit
                bestIter = populationData.generationNumber
                bestSolution = best.data
                bestFitnessFunctionCount = evaluator.cnt
            }
        }

        val terminate = GenerationCount(generations)
        algorithm.evolve(populationSize, populationSize / 6, terminate)
        println("===================")
        println("Best fit: $bestValue")
        println("Best iteration: $bestIter")
        println("Best solution: $bestSolution")
        println("Best Fitness Function Count: $bestFitnessFunctionCount")
        if (problemSize < 10) {
            val ans = MutableList(problemSize) { x ->
                MutableList(problemSize) { y ->
                    if ((x + y) % 2 == 0) " - " else " + "
                }
            }
            for ((x1, y1) in bestSolution.withIndex()) {
                ans[x1][y1] = " O "
            }
            ans.forEach { it.forEach(::print); println() }
        }
        println(evaluator.cnt)
    }
}