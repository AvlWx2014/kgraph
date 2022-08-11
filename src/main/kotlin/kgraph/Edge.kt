package kgraph

interface Edge<V : Any, E : Any> {
    enum class Direction { UNDIRECTED, DIRECTED }

    val u: Vertex<V>
    val v: Vertex<V>
    val value: E
    val direction: Direction
}

data class UndirectedEdge<V : Any, E : Any>(
    override val u: Vertex<V>,
    override val v: Vertex<V>,
    override val value: E
) : Edge<V, E> {
    override val direction = Edge.Direction.UNDIRECTED

    override fun toString(): String = "$u -- $value -- $v"
}

data class DirectedEdge<V : Any, E : Any>(
    override val u: Vertex<V>,
    override val v: Vertex<V>,
    override val value: E
) : Edge<V, E> {
    override val direction = Edge.Direction.DIRECTED
    val source = u
    val target = v

    override fun toString(): String = "$u -- $value --> $v"
}

