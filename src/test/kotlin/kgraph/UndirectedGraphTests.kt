package kgraph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UndirectedGraphTests {

    @Test
    fun testEmptyGraph() {
        val G = UndirectedGraph<String, String>()
        assertEquals(0, G.n, "Empty graph should have 0 vertices")
        assertEquals(0, G.m, "Empty graph should have 0 eGes")
    }

    @Test
    fun testIndependentSet() {
        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        val G = buildUndirectedGraph<String, Int> {
            addVertex(u)
            addVertex(v)
            addVertex(w)
        }
        assertEquals(3, G.n, "Independent set of size 3 should have 3 vertices")
        assertEquals(0, G.m, "Independent set of size 3 should have 0 eGes")
        listOf(u, v, w).forEach {
            assertEquals(0, G.neighbors(it).size, "In an Independent Set, no vertex should have neighbors")
        }
    }

    @Test
    fun testCycle() {
        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        val G = buildGraph<String, Int> {
            addEdge(undirectedEdge {
                from = u
                to = v
                edgeValue = 1
            })
            addEdge(undirectedEdge {
                from = v
                to = w
                edgeValue = 1
            })
            addEdge(undirectedEdge {
                from = w
                to = u
                edgeValue = 1
            })
        }

        assertEquals(3, G.n, "C3 should have 3 vertices")
        assertEquals(3, G.m, "C3 should have 3 eGes")

        listOf(u, v, w).forEach {
            assertEquals(2, G.degree(it), "Each vertex in C3 should only have degree 1")
        }

        assertTrue("v and w should both be neighbors of u") { v in G.neighbors(u) && w in G.neighbors(u) }
        assertTrue("u and w should both be neighbors of v") { u in G.neighbors(w) && w in G.neighbors(v) }
        assertTrue("u and v should both be neighbors of w") { u in G.neighbors(w) && v in G.neighbors(w) }
    }
}