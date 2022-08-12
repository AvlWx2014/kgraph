package kgraph

class MutableDirectedGraph<V : Any, E : Any> : DirectedGraph<V, E>(), MutableGraph<V, E> {

    override fun addVertex(vertex: Vertex<V>): Boolean {
        if (vertex in vertexMap) {
            return false
        }

        vertexMap[vertex] = vertex
        return true
    }

    override fun removeVertex(vertex: Vertex<V>): Boolean {
        if (vertex !in vertexMap) {
            return false
        }

        vertexMap.remove(vertex)?.also { it.disconnect() }

        // TODO: consider a deep copy of the edges incident on [vertex]
        // TODO: and delegating to removeEdge to delete them
        edgeMap.entries.removeIf { (e, _) -> e.u == vertex || e.v == vertex }

        return true
    }

    override fun addEdge(u: V, v: V, value: E): Boolean = addEdge(Vertex(u), Vertex(v), value)
    override fun addEdge(u: Vertex<V>, v: Vertex<V>, value: E): Boolean = addEdge(DirectedEdge(u, v, value))
    override fun addEdge(edge: Edge<V, E>): Boolean {
        require(edge is DirectedEdge) {
            "A directed graph implementation cannot add an undirected edge to the graph"
        }

        if (edge in edgeMap) {
            return false
        }

        if (edge.u !in vertexMap) {
            addVertex(edge.u)
        }

        if (edge.v !in vertexMap) {
            addVertex(edge.v)
        }

        edgeMap[edge] = edge

        val u = vertexMap[edge.u] ?: error("${edge.u} is missing from the graph")
        val v = vertexMap[edge.v] ?: error("${edge.v} is missing from the graph")

        u.addNeighbor(v, edge)
        v.addPredecessor(u, edge)

        return true
    }

    override fun removeEdge(edge: Edge<V, E>): Boolean {
        if (edge !is DirectedEdge || edge !in edgeMap) {
            return false
        }

        edgeMap -= edge

        val u = vertexMap[edge.u] ?: error("${edge.u} is missing from the graph")
        val v = vertexMap[edge.v] ?: error("${edge.v} is missing from the graph")

        u.removeNeighbor(v)
        v.removePredecessor(u)

        return true
    }
}