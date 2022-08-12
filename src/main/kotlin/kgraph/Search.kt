package kgraph

private const val UNDISCOVERED = -1L
private const val UNFINISHED = -1L

private const val CODEPOINT_PI_UPPER = 0x03A0
private const val CODEPOINT_PI_LOWER = 0x03C0

/**
 * Wrapper class for Vertex instances, which maintains attributes
 * related to performing depth-first search according to the
 * pseudocode outlined in CLRS Introduction to Algorithms (pg. 604).
 */
@Experimental
private class DfsVertexWrapper(val vertex: Vertex<*>) {
    var status: Status = Status.UNDISCOVERED
    var pi: Vertex<*>? = null
    var d: Long = UNDISCOVERED
    var f: Long = UNFINISHED

    override fun toString(): String {
        return """VERTEX: $vertex
            |STATUS: $status
            |${CODEPOINT_PI_UPPER.toChar()}: $pi
            |DISCOVERED: $d
            |FINISHED: $f
            |
        """.trimMargin()
    }
}

private enum class Status { UNDISCOVERED, DISCOVERED, COMPLETE }

sealed class DfsResult<out T> {
    data class Found<out T>(val target: T) : DfsResult<T>()
    object CycleDetected : DfsResult<Nothing>()
    object NotFound : DfsResult<Nothing>()
}

fun <V : Any, E : Any> DirectedGraph<V, E>.find(target: Vertex<V>?) = dfs(target)
fun <V : Any, E : Any> DirectedGraph<V, E>.search(target: Vertex<V>?) = dfs(target)

/**
 * An implementation of depth-first search on a directed graph.
 *
 * Adapted from pseudocode presented in CLRS Introduction to Algorithms (pg. 604).
 *
 * Since vertices have no concept of discovery time, finish time, color, or predecessor
 * this implementation wraps all the vertices in the graph in a `DfsVertexWrapper` for
 * the duration.
 *
 * This implementation of DFS both searches for [target] in the graph,
 * but also detects cycles. If a cycle is detected in the graph before [target]
 * is found (or conclusively not found) then an answer of `DfsResult.CycleDetected`
 * is returned.
 */
@Experimental
fun <V : Any, E : Any> DirectedGraph<V, E>.dfs(target: Vertex<V>?): DfsResult<Vertex<V>> {
    val visited = vertices.associateWith(::DfsVertexWrapper)
    var time = 0L

    val startingVertices = roots().ifEmpty { visited.keys }

    fun _dfs(u: Vertex<V>): DfsResult<Vertex<V>> {
        if (u == target) {
            return DfsResult.Found(u)
        }

        val wrapper = visited[u] ?: error("Catastrophic failure: vertex wrapper is missing from the mapping.")
        wrapper.d = ++time
        wrapper.status = Status.DISCOVERED
        u.neighbors.forEach { v ->
            val nbr = visited[v] ?: error("Catastrophic failure: vertex wrapper is missing from the mapping.")

            // if we come across a GREY neighbor that means we've
            // already started visiting that node. We must have
            // found a cycle
            if (nbr.status == Status.DISCOVERED) {
                return DfsResult.CycleDetected
            }

            if (nbr.status == Status.UNDISCOVERED) {
                nbr.pi = u
                return _dfs(v)
            }
        }

        wrapper.status = Status.COMPLETE
        wrapper.f = ++time
        return DfsResult.NotFound
    }

    var result: DfsResult<Vertex<V>> = DfsResult.NotFound
    for (vertex in startingVertices) {
        val wrapper = visited[vertex] ?: error("Catastrophic failure: vertex wrapper is missing from the mapping.")
        if (wrapper.status == Status.UNDISCOVERED) {
            result = _dfs(vertex)
        }

        if (result is DfsResult.Found || result is DfsResult.CycleDetected) {
            break
        }
    }

    return result
}

/**
 * Detect cycles in a directed graph using depth-first search.
 */
@Experimental
fun DirectedGraph<*, *>.detectCycles(): Boolean = dfs(null) is DfsResult.CycleDetected
