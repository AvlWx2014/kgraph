package kgraph

typealias Digraph<V, E> = DirectedGraph<V, E>

fun <V: Any, E: Any> buildDigraph(block: MutableDirectedGraph<V, E>.() -> Unit) = buildDirectedGraph(block)
fun <V: Any, E: Any> buildDirectedGraph(block: MutableDirectedGraph<V, E>.() -> Unit): DirectedGraph<V, E> {
    return MutableDirectedGraph<V, E>().apply(block)
}

open class DirectedGraph<V : Any, E : Any> internal constructor() : Graph<V, E> {

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

fun <V: Any, E : Any> DirectedGraph<V, E>.inDegree(vertex: Vertex<V>) = vertex.inDegree()
fun <V: Any, E : Any> DirectedGraph<V, E>.outDegree(vertex: Vertex<V>) = vertex.outDegree()

fun <V : Any, E : Any> DirectedGraph<V, E>.sources(): Collection<Vertex<V>> = roots()
fun <V : Any, E : Any> DirectedGraph<V, E>.roots(): Collection<Vertex<V>> = vertices.filter { this.inDegree(it) == 0 }

fun <V : Any, E : Any> DirectedGraph<V, E>.sinks(): Collection<Vertex<V>> = leaves()
fun <V : Any, E : Any> DirectedGraph<V, E>.leaves(): Collection<Vertex<V>> = vertices.filter { this.outDegree(it) == 0 }
