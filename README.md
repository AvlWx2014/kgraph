# KGraph
The motivation for this library is to provide a set of interfaces for and implementations of graph data structures. It 
provides interfaces for both mutable and immutable graphs, as well as implementations of undirected and directed graphs
(in both mutable and immutable flavors).

It additionally features:
* A builder DSL for constructing immutable graphs
* A builder DSL for constructing Edge instances
* A number of extension functions that are useful for working with directed graphs

### Undirected ###
Below is an example of constructing the complete graph K3 as an immutable, undirected graph using the builder DSL.

```kotlin
val u = Vertex("u")
val v = Vertex("v")
val w = Vertex("w")

val uv = undirectedEdge<String, String> {
    from = u
    to = v
    edgeValue = "Arbitrary Label"
}

val vw = undirectedEdge<String, String> {
    from = v
    to = w
    edgeValue = "VW"
}

val wu = undirectedEdge<String, String> {
    from = w
    to = u
    edgeValue = "UW"
}

val G = buildUndirectedGraph<String, String> {
    addEdge(uv)
    addEdge(vw)
    addEdge(wu)
}

println("Graph G has ${G.n} vertices and ${G.m} edges.")

// Graph G has 3 vertices and 3 edges.
```

Below is an example of constructing the path graph P2 as a mutable, undirected graph.

```kotlin
val G = MutableUndirectedGraph<String, String>()

val u = Vertex("u")
val v = Vertex("v")
val w = Vertex("w")

// here, we don't need to parameterize the undirectedEdge call
// the compiler can infer if from G's parameterized types
G += undirectedEdge {
    from = u
    to = v
    edgeValue = "UV"
}

// the += operator is overridden to provide convenient syntax
// but using += is the same as calling addEdge(Edge<V, E>) 
// directly
G += undirectedEdge {
    from = v
    to = w
    edgeValue = "VW"
}

println("Graph G has ${G.n} vertices and ${G.m} edges.")

// Graph G has 3 vertices and 2 edges.
```

### Directed ###
Below is an example of constructing the cycle graph C3 as an immutable, directed graph using the builder DSL.

```kotlin
val u = Vertex("u")
val v = Vertex("v")
val w = Vertex("w")

val uv = directedEdge<String, String> {
    from = u
    to = v
    edgeValue = "UV"
}

val vw = directedEdge<String, String> {
    from = v
    to = w
    edgeValue = "VW"
}

val wu = directedEdge<String, String> {
    from = w
    to = u
    edgeValue = "WU"
}

val G = buildDirectedGraph<String, String> {
    addEdge(uv)
    addEdge(vw)
    addEdge(wu)
}

println("Graph G has ${G.n} vertices and ${G.m} edges.")

// Graph G has 3 vertices and 3 edges.
```

Below is an example of constructing the path graph P3 as a mutable, directed graph.

```kotlin
val G = MutableDirectedGraph<String, String>()

val u = Vertex("u")
val v = Vertex("v")
val w = Vertex("w")
val y = Vertex("y")

// here, we don't need to parameterize the directedEdge call
// the compiler can infer if from G's parameterized types
G += directedEdge {
    from = u
    to = v
    edgeValue = "UV"
}

// the += operator is overridden to provide convenient syntax
// but using += is the same as calling addEdge(Edge<V, E>) 
// directly
G += directedEdge {
    from = v
    to = w
    edgeValue = "VW"
}

G += directedEdge {
    from = w
    to = y
    edgeValue = "WY"
}

println("Graph G has ${G.n} vertices and ${G.m} edges.")

// Graph G has 4 vertices and 3 edges.
```

#### Useful Extensions ####

##### Roots (Sources) #####
Getting "root" or "source" nodes (i.e. vertices with no incoming edges) from a directed graph:
```kotlin
// build the graph
val G = MutableDirectedGraph<String, String>()

G += directedEdge {
    from = Vertex("u")
    to = Vertex("v")
    edgeValue = "UV"
}

G += directedEdge {
    from = Vertex("v")
    to = Vertex("w")
    edgeValue = "VW"
}

G += directedEdge {
    from = Vertex("w")
    to = Vertex("y")
    edgeValue = "WY"
}

var roots = G.roots()
println("$roots")
// [( u )]

roots = G.sources()
println("$roots")
// [( u )]
```

##### Leaves (Sinks) #####
Getting "leaf" or "sink" vertices (i.e. vertices with no outgoing edges) from a directed graph:
```kotlin
// build the graph
val G = MutableDirectedGraph<String, String>()

G += directedEdge {
    from = Vertex("u")
    to = Vertex("v")
    edgeValue = "UV"
}

G += directedEdge {
    from = Vertex("u")
    to = Vertex("w")
    edgeValue = "UW"
}

G += directedEdge {
    from = Vertex("u")
    to = Vertex("y")
    edgeValue = "UY"
}

var leaves = G.leaves()
println("$leaves")
// [( v ), ( w ), ( y )]

leaves = G.sinks()
println("$leaves")
// [( v ), ( w ), ( y )]
```

##### Degree, In-Degree, and Out-Degree #####
When working with directed graphs, the concept of the degree of a vertex is often split into two concepts:
in-degree and out-degree. These are defined as the number of incoming and outgoing edges (respectively) 
incident to a vertex. 

In the implementation of a directed graph in this library, the degree of a vertex in a directed graph is
the sum of its in-degree and its out-degree i.e. $`deg(v) = deg^{-}(v) + deg^{+}(v)`$

Here is how you get those values from a directed graph:
```kotlin
// build the graph
val G = MutableDirectedGraph<String, String>()

G += directedEdge {
    from = Vertex("u")
    to = Vertex("v")
    edgeValue = "UV"
}

G += directedEdge {
    from = Vertex("u")
    to = Vertex("w")
    edgeValue = "UW"
}

G += directedEdge {
    from = Vertex("u")
    to = Vertex("y")
    edgeValue = "UY"
}

G.vertices.forEach {
    val msg = """Vertex: $it 
                |In Degree: ${G.inDegree(it)} 
                |Out Degree: ${G.outDegree(it)}
                |Degree: ${G.degree(it)}
                |""".trimMargin()
    println(msg)
}
/**
Vertex: ( u )
In Degree: 0
Out Degree: 3
Degree: 3

Vertex: ( v )
In Degree: 1
Out Degree: 0
Degree: 1

Vertex: ( w )
In Degree: 1
Out Degree: 0
Degree: 1

Vertex: ( y )
In Degree: 1
Out Degree: 0
Degree: 1
 */
```

##### Directed Graph vs Digraph #####
For anybody who likes the name Digraph, the library defines two typealiases you might find more friendly:
* `Digraph<V, E>` :arrow_right: `DirectedGraph<V, E>`
* `MutableDigraph<V, E>` :arrow_right: `MutableDirectedGraph<V, E>`

And in code:
```kotlin
// these are equivalent
val mdg = MutableDirectedGraph<String, Int>()
val mdig = MutableDigraph<String, Int>()

// these are also equivalent
val dg: DirectedGraph<String, Int> = buildDirectedGraph{}
val dig: Digraph<String, Int> = buildDirectedGraph{}
```