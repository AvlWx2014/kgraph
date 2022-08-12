package kgraph

fun <V : Any, E : Any> directedEdge(block: EdgeBuilder<V, E>.() -> Unit): DirectedEdge<V, E> {
    return EdgeBuilder<V, E>(Edge.Direction.DIRECTED).apply(block).build() as DirectedEdge
}

fun <V : Any, E : Any> undirectedEdge(block: EdgeBuilder<V, E>.() -> Unit): UndirectedEdge<V, E> {
    return EdgeBuilder<V, E>(Edge.Direction.UNDIRECTED).apply(block).build() as UndirectedEdge
}

class EdgeBuilder<V : Any, E : Any> internal constructor(private val direction: Edge.Direction) {

    lateinit var from: Vertex<V>
    lateinit var to: Vertex<V>
    lateinit var edgeValue: E

    fun build(): Edge<V, E> {
        check(::from.isInitialized) { "Source vertex is uninitialized" }
        check(::to.isInitialized) { "Terminating vertex is uninitialized" }
        check(::edgeValue.isInitialized) { "Edge value was never specified" }
        
        return when (direction) {
            Edge.Direction.UNDIRECTED -> UndirectedEdge(from, to, edgeValue)
            Edge.Direction.DIRECTED -> DirectedEdge(from, to, edgeValue)
        }
    }
}