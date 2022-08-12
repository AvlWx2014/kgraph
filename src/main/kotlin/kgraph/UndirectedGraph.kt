package kgraph

fun <V: Any, E: Any> buildGraph(block: MutableUndirectedGraph<V, E>.() -> Unit) = buildUndirectedGraph(block)
fun <V: Any, E: Any> buildUndirectedGraph(block: MutableUndirectedGraph<V, E>.() -> Unit): UndirectedGraph<V, E> {
    return MutableUndirectedGraph<V, E>().apply(block)
}

open class UndirectedGraph<V : Any, E : Any> internal constructor() : Graph<V, E> {

    internal val vertexMap = mutableMapOf<Vertex<V>, Vertex<V>>()
    override val vertices: Collection<Vertex<V>>
        get() = vertexMap.values

    internal val edgeMap = mutableMapOf<Edge<V, E>, Edge<V, E>>()
    override val edges: Collection<Edge<V, E>>
        get() = edgeMap.values

    override fun neighbors(vertex: Vertex<V>): Collection<Vertex<V>> {
        return vertexMap[vertex]?.neighbors ?: error("Vertex $vertex is not in the graph")
    }

    override fun degree(vertex: Vertex<V>): Int = vertexMap[vertex]?.degree() ?: 0
}