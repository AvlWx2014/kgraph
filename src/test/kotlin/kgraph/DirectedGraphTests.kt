package kgraph

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DirectedGraphTests {

    @Test
    fun testEmptyGraph() {
        val dg = DirectedGraph<String, String>()
        assertEquals(0, dg.n, "Empty graph should have 0 vertices")
        assertEquals(0, dg.m, "Empty graph should have 0 edges")
    }

    @Test
    fun testIndependentSet() {
        val u = Vertex("u")
        val v = Vertex("v")
        val w = Vertex("w")

        val dg = buildDirectedGraph<String, Int> {
            addVertex(u)
            addVertex(v)
            addVertex(w)
        }

        assertEquals(3, dg.n, "Independent set of size 3 should have 3 vertices")
        assertEquals(0, dg.m, "Independent set of size 3 should have 0 edges")
        listOf(u, v, w).forEach {
            assertEquals(0, dg.neighbors(it).size, "In an Independent Set, no vertex should have neighbors")
        }
    }

    @Test
    fun testCycle() {
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

        val dg = buildDirectedGraph<String, Int> {
            addEdge(uv)
            addEdge(vw)
            addEdge(wu)
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
        // build a trie for testing a tree-like structure
        // using the words bet and cat
        // probably doesn't make sense to have a directed graph as a trie, but
        // it's contrived just for tests
        val T = buildDigraph<String, String> {
            addEdge(
                directedEdge {
                    from = Vertex("b")
                    to = Vertex("e")
                    edgeValue = "e"
                }
            )

            addEdge(
                directedEdge {
                    from = Vertex("c")
                    to = Vertex("a")
                    edgeValue = "a"
                }
            )

            val t =  Vertex("t")

            addEdge(
                directedEdge {
                    from = Vertex("e")
                    to = t
                    edgeValue = "t"
                }
            )

            addEdge(
                directedEdge {
                    from = Vertex("a")
                    to = t
                    edgeValue = "t"
                }
            )
        }

        val leaves = T.leaves()
        assertEquals(1, leaves.size, "Expected tree to have only 1 leaf vertex")
        assertContains(leaves, Vertex("t"), "Expected t as the only leaf node")

        val roots = T.roots()
        assertEquals(2, roots.size, "Expected tree to have 2 root vertices")
        assertContains(roots, Vertex("b"), "Expected b as a root node")
        assertContains(roots, Vertex("c"), "Expected c as a root node")
    }
}