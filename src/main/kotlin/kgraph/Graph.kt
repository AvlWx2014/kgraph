package kgraph

interface Graph<V : Any, E : Any> {
    val vertices: Collection<Vertex<V>>
    val edges: Collection<Edge<V, E>>

    fun neighbors(vertex: Vertex<V>): Collection<Vertex<V>>
    fun degree(vertex: Vertex<V>): Int
}

val <V : Any, E : Any> Graph<V, E>.n: Int
    get() = this.vertices.size

val <V : Any, E : Any> Graph<V, E>.m: Int
    get() = this.edges.size

val <V : Any, E : Any> Graph<V, E>.numberOfVertices: Int
    get() = n

val <V : Any, E : Any> Graph<V, E>.numberOfEdges: Int
    get() = m