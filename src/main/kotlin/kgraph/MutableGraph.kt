package kgraph

interface MutableGraph<V : Any, E : Any> {
    fun addEdge(u: V, v: V, value: E): Boolean
    fun addEdge(u: Vertex<V>, v: Vertex<V>, value: E): Boolean
    fun addEdge(edge: Edge<V, E>): Boolean
    fun removeEdge(edge: Edge<V, E>): Boolean
    fun addVertex(vertex: Vertex<V>): Boolean
    // TODO: removing vertices is VERY slow
    // TODO: see if there's some sort of optimization for this
    fun removeVertex(vertex: Vertex<V>): Boolean
}

operator fun <V : Any> MutableGraph<V, *>.plusAssign(vertex: Vertex<V>) { this.addVertex(vertex) }
operator fun <V : Any, E : Any> MutableGraph<V, E>.plusAssign(edge: Edge<V, E>) { this.addEdge(edge) }
operator fun <V : Any> MutableGraph<V, *>.minusAssign(vertex: Vertex<V>) { this.removeVertex(vertex) }
operator fun <V : Any, E : Any> MutableGraph<V, E>.minusAssign(edge: Edge<V, E>) { this.removeEdge(edge) }