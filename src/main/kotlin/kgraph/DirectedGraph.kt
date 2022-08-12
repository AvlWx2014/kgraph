package kgraph

open class DirectedGraph<V : Any, E : Any> : Graph<V, E> {

    internal val vertexMap = mutableMapOf<Vertex<V>, Vertex<V>>()
    override val vertices: Collection<Vertex<V>>
        get() = vertexMap.values

    internal val edgeMap = mutableMapOf<Edge<V, E>, Edge<V, E>>()
    override val edges: Collection<Edge<V, E>>
        get() = edgeMap.values

    override fun neighbors(vertex: Vertex<V>): Collection<Vertex<V>> {
        return vertexMap[vertex]?.neighbors ?: error("Vertex $vertex is not in the graph")
    }

    override fun degree(vertex: Vertex<V>): Int {
        val inDegree = vertexMap[vertex]?.inDegree() ?: 0
        val outDegree = vertexMap[vertex]?.outDegree() ?: 0
        return inDegree + outDegree
    }
}
