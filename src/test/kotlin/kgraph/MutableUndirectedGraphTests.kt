package kgraph

import kotlin.test.*

class MutableUndirectedGraphTests {

    @Test
    fun testEmptyGraph() {
        val G = MutableUndirectedGraph<String, String>()
        assertEquals(0, G.n, "Empty graph should have 0 vertices")
        assertEquals(0, G.m, "Empty graph should have 0 edges")
    }

    @Test
    fun testIndependentSet() {
        val G = MutableUndirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        G.addVertex(u)
        G.addVertex(v)
        G.addVertex(w)

        assertEquals(3, G.n, "Independent set of size 3 should have 3 vertices")
        assertEquals(0, G.m, "Independent set of size 3 should have 0 edges")
        listOf(u, v, w).forEach {
            assertEquals(0, G.neighbors(it).size, "In an Independent Set, no vertex should have neighbors")
        }
    }

    @Test
    fun testCycle() {
        val G = MutableUndirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        G.addEdge(u, v, 1)
        G.addEdge(v, w, 1)
        G.addEdge(w, u, 1)

        assertEquals(3, G.n, "C3 should have 3 vertices")
        assertEquals(3, G.m, "C3 should have 3 edges")

        listOf(u, v, w).forEach {
            assertEquals(2, it.degree(), "Each vertex in C3 should have degree 1")
        }

        assertTrue("v and w should both be neighbors of u") { v in G.neighbors(u) && w in G.neighbors(u) }
        assertTrue("u and w should both be neighbors of v") { u in G.neighbors(w) && w in G.neighbors(v) }
        assertTrue("u and v should both be neighbors of w") { u in G.neighbors(w) && v in G.neighbors(w) }
    }

    @Test
    fun testTree() {
        val G = MutableUndirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")
        val x = Vertex("x")
        val y = Vertex("y")

        G.addEdge(u, v, 1)
        G.addEdge(u, w, 1)
        G.addEdge(v, x, 1)
        G.addEdge(w, x, 1)
        G.addEdge(x, y, 1)

        assertEquals(5, G.n, "Unexpected number of vertices in the graph")
        assertEquals(5, G.m, "Unexpected number of edges in the graph")

        assertEquals(2, u.degree(), "Root vertex should have 2 incident edges")
        listOf(v, w).forEach {
            assertEquals(2, it.degree(), "Vertex $it should have only 3 incident edges")
        }
        assertEquals(3, x.degree(), "Vertex $x should have degree 3")
        assertEquals(1, y.degree(), "Vertex $y should have degree 1")
    }

    @Test
    fun testTreeWithoutPreconstructedVertices() {
        val G = MutableUndirectedGraph<String, Int>()

        G.addEdge("u", "v", 1)
        G.addEdge("u", "w", 1)
        G.addEdge("v", "x", 1)
        G.addEdge("w", "x", 1)
        G.addEdge("x", "y", 1)

        assertEquals(5, G.n, "Unexpected number of vertices in the graph")
        assertEquals(5, G.m, "Unexpected number of edges in the graph")
    }

    @Test
    fun testAddingDirectedEdgeFails() {
        val G = MutableUndirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")

        assertFails {
            G += directedEdge {
                from = u
                to = v
                edgeValue = 1
            }
        }
    }

    @Test
    fun testAddingDuplicateEdgeFails() {
        val G = MutableUndirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")

        assertTrue("Unexpectedly unable to add edge from $u to $v") {
            G.addEdge(
                undirectedEdge {
                    from = u
                    to = v
                    edgeValue = 1
                }
            )
        }

        assertFalse("Unexpectedly able to add duplicate edge from $u to $v") {
            G.addEdge(
                undirectedEdge {
                    from = u
                    to = v
                    edgeValue = 1
                }
            )
        }
    }

    @Test
    fun testRemovingEdges() {
        val G = MutableUndirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        G += undirectedEdge {
            from = u
            to = v
            edgeValue = 1
        }

        val vw = undirectedEdge<String, Int> {
            from = v
            to = w
            edgeValue = 1
        }

        G += vw

        assertEquals(3, G.n, "P2 should have 3 vertices")
        assertEquals(2, G.m, "P2 should have 2 edges")
        assertEquals(2, v.degree(), "Vertex $v should have degree 2")
        listOf(u, w).forEach {
            assertEquals(1, it.degree(), "Vertex $it should have degree 1")
        }

        G -= vw

        assertEquals(3, G.n, "Graph should have 3 vertices")
        assertEquals(1, G.m, "Graph should have only 1 edge")

        listOf(u, v).forEach {
            assertEquals(1, it.degree(), "Vertex $it should have degree 1")
        }
        assertEquals(0, w.degree(), "Vertex $w should have degree 0")
    }

    @Test
    fun testRemovingVertices() {
        val G = MutableUndirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        val uv = undirectedEdge<String, Int> {
            from = u
            to = v
            edgeValue = 1
        }

        val vw = undirectedEdge<String, Int> {
            from = v
            to = w
            edgeValue = 1
        }

        val wu = undirectedEdge<String, Int> {
            from = w
            to = u
            edgeValue = 1
        }

        G += uv
        G += vw
        G += wu

        assertEquals(3, G.n, "C3 should have 3 vertices")
        assertEquals(3, G.m, "C3 should have 3 edges")
        listOf(u, v, w).forEach {
            assertEquals(2, it.degree(), "Vertex $it should have degree 2 in C3")
        }

        G -= v

        assertEquals(2, G.n, "Graph should only have 2 vertices")
        assertEquals(1, G.m, "Graph should have only 1 edge")

        assertEquals(1, w.degree(), "Vertex $w should have degree 1")
        assertEquals(1, u.degree(), "Vertex $u should have degree 1")
    }
}