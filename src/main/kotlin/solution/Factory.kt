package solution

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory
import java.util.*

class Factory(private val problemSize: Int) : AbstractCandidateFactory<Solution>() {
    override fun generateRandomCandidate(random: Random): Solution =
        Solution(MutableList(problemSize) { it }.shuffled())
}