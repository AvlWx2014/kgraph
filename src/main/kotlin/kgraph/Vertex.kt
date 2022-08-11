package kgraph

data class Vertex<V : Any>(val value: V) {

    /**
     * Keep track of neighbors of this vertex to make degree computation simpler.
     */
    private val adjacencyMap = mutableMapOf<Vertex<V>, Edge<V, *>>()
    val neighbors: Collection<Vertex<V>>
        get() = adjacencyMap.keys

    internal fun addNeighbor(vertex: Vertex<V>, edge: Edge<V, *>) {
        if (vertex !in adjacencyMap) {
            adjacencyMap[vertex] = edge
        }
    }

    internal fun removeNeighbor(vertex: Vertex<V>) { adjacencyMap -= vertex }

    /**
     * Keep track of predecessors of this vertex to make in-degree
     * computation easier.
     *
     * In an undirected graph, predecessors and neighbors are the
     * same since there's no sense of directionality or orientation
     * of the edges.
     */
    private val predecessorMap = mutableMapOf<Vertex<V>, Edge<V, *>>()
    val predecessors: Collection<Vertex<V>>
        get() = predecessorMap.keys

    internal fun addPredecessor(vertex: Vertex<V>, edge: Edge<V, *>) {
        if (vertex !in predecessorMap) {
            predecessorMap[vertex] = edge
        }
    }

    internal fun removePredecessor(vertex: Vertex<V>) { predecessorMap -= vertex }

    internal fun degree(): Int = adjacencyMap.size
    internal fun inDegree(): Int = predecessorMap.size
    internal fun outDegree(): Int = adjacencyMap.size

    override fun toString(): String = "( $value )"
}

internal fun <V : Any> Vertex<V>.disconnect() {
    this.neighbors.forEach{ it.removePredecessor(this) }
    this.predecessors.forEach { it.removeNeighbor(this) }
}