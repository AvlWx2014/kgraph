package kgraph

import kotlin.test.*

class MutableDirectedGraphTests {

    @Test
    fun testEmptyGraph() {
        val dg = MutableDirectedGraph<String, String>()
        assertEquals(0, dg.n, "Empty graph should have 0 vertices")
        assertEquals(0, dg.m, "Empty graph should have 0 edges")
    }

    @Test
    fun testIndependentSet() {
        val dg = MutableDirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        dg += u
        dg += v
        dg += w

        assertEquals(3, dg.n, "Independent set of size 3 should have 3 vertices")
        assertEquals(0, dg.m, "Independent set of size 3 should have 0 edges")
        listOf(u, v, w).forEach {
            assertEquals(0, dg.neighbors(it).size, "In an Independent Set, no vertex should have neighbors")
        }
    }

    @Test
    fun testCycle() {
        val dg = MutableDirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        val uv: DirectedEdge<String, Int> = directedEdge {
            from = u
            to = v
            edgeValue = 1
        }

        val vw = directedEdge<String, Int> {
            from = v
            to = w
            edgeValue = 1
        }

        dg += uv
        dg += vw
        dg += directedEdge {
            from = w
            to = u
            edgeValue = 1
        }

        assertEquals(3, dg.n, "C3 should have 3 vertices")
        assertEquals(3, dg.m, "C3 should have 3 edges")

        listOf(u, v, w).forEach {
            assertEquals(2, dg.degree(it), "Each vertex in C3 should only have degree 1")
        }

        assertTrue("v should be a neighbor of u") { v in dg.neighbors(u) }
        assertTrue("w should be a neighbor of v") { w in dg.neighbors(v) }
        assertTrue("u should be a neighbor of w") { u in dg.neighbors(w) }
    }

    @Test
    fun testTree() {
        val dg = MutableDirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")
        val x = Vertex("x")
        val y = Vertex("y")

        dg.addEdge(u, v, 1)
        dg.addEdge(u, w, 1)
        dg.addEdge(v, x, 1)
        dg.addEdge(w, x, 1)
        dg.addEdge(x, y, 1)

        assertEquals(5, dg.n, "Unexpected number of vertices in the graph")
        assertEquals(5, dg.m, "Unexpected number of edges in the graph")

        assertEquals(2, dg.degree(u), "Root vertex should have 2 outgoing edges")
        listOf(v, w).forEach {
            assertEquals(2, dg.degree(it), "Vertex $it should have only 1 outgoing edge")
        }
        assertEquals(2, x.inDegree(), "Vertex $x should have in-degree 2")
        assertEquals(1, x.outDegree(), "Vertex $x should have out-degree 1")
        assertEquals(1, y.inDegree(), "Vertex $y should have in-degree 1")
        assertEquals(0, y.outDegree(), "Vertex $y should be a leaf")
    }

    @Test
    fun testTreeWithoutPreconstructedVertices() {
        val dg = MutableDirectedGraph<String, Int>()

        dg.addEdge("u", "v", 1)
        dg.addEdge("u", "w", 1)
        dg.addEdge("v", "x", 1)
        dg.addEdge("w", "x", 1)
        dg.addEdge("x", "y", 1)

        assertEquals(5, dg.n, "Unexpected number of vertices in the graph")
        assertEquals(5, dg.m, "Unexpected number of edges in the graph")
    }

    @Test
    fun testAddingUndirectedEdgeFails() {
        val dg = MutableDirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")

        assertFails {
            dg += undirectedEdge {
                from = u
                to = v
                edgeValue = 1
            }
        }
    }

    @Test
    fun testAddingDuplicateEdgeFails() {
        val dg = MutableDirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")

        assertTrue("Unexpectedly unable to add edge from $u to $v") {
            dg.addEdge(
                directedEdge {
                    from = u
                    to = v
                    edgeValue = 1
                }
            )
        }

        assertFalse("Unexpectedly able to add duplicate edge from $u to $v") {
            dg.addEdge(
                directedEdge {
                    from = u
                    to = v
                    edgeValue = 1
                }
            )
        }
    }

    @Test
    fun testRemovingEdges() {
        val dg = MutableDirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        dg += directedEdge {
            from = u
            to = v
            edgeValue = 1
        }

        val vw = directedEdge<String, Int> {
            from = v
            to = w
            edgeValue = 1
        }

        dg += vw

        assertEquals(3, dg.n, "P2 should have 3 vertices")
        assertEquals(2, dg.m, "P2 should have 2 edges")

        listOf(u, v).forEach {
            assertEquals(1, it.outDegree(), "Vertex $it should have 1 outgoing edge")
        }
        listOf(v, w).forEach {
            assertEquals(1, it.inDegree(), "Vertex $it should have 1 incoming edge")
        }

        dg -= vw

        assertEquals(3, dg.n, "Graph should have 3 vertices")
        assertEquals(1, dg.m, "Graph should have only 1 edge")

        assertEquals(1, u.outDegree(), "Vertex $u should have 1 outgoing edge")
        assertEquals(1, v.inDegree(), "Vertex $v should have 1 incoming edge")

        listOf(w::inDegree, w::outDegree).forEach {
            assertEquals(0, it(), "Vertex $w should have no incident edges")
        }
    }

    @Test
    fun testRemovingVertices() {
        val dg = MutableDirectedGraph<String, Int>()

        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        val uv = directedEdge<String, Int> {
            from = u
            to = v
            edgeValue = 1
        }

        val vw = directedEdge<String, Int> {
            from = v
            to = w
            edgeValue = 1
        }

        val wu = directedEdge<String, Int> {
            from = w
            to = u
            edgeValue = 1
        }

        dg += uv
        dg += vw
        dg += wu

        assertEquals(3, dg.n, "C3 should have 3 vertices")
        assertEquals(3, dg.m, "C3 should have 3 edges")

        dg -= v

        assertEquals(2, dg.n, "Graph should only have 2 vertices")
        assertEquals(1, dg.m, "Graph should have only 1 edge")

        assertEquals(0, w.inDegree(), "Vertex $w should have no incoming edges")
        assertEquals(1, w.outDegree(), "Vertex $w should have 1 outgoing edge")
        assertEquals(1, u.inDegree(), "Vertex $u should have 1 incoming edge")
        assertEquals(0, u.outDegree(), "Vertex $u should have no outgoing edges")
    }
}