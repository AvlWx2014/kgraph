package kgraph

import kotlin.test.*

class SearchTests {

    @Test
    fun testDfsOnIndependentSet() {
        val G = buildDirectedGraph<Int, Int> {
            addVertex(Vertex(1))
            addVertex(Vertex(2))
            addVertex(Vertex(3))
        }

        val target = Vertex(2)
        val result = G.dfs(target)
        assertIs<DfsResult.Found<Vertex<Int>>>(result)
        assertEquals(result.target, target)
    }

    @Test
    fun testDfsOnTree() {
        // use the same silly directed trie for this test
        // TODO: use a better example
        val T = buildDigraph<String, String> {
            addEdge(directedEdge {
                from = Vertex("b")
                to = Vertex("e")
                edgeValue = "e"
            })

            addEdge(directedEdge {
                from = Vertex("c")
                to = Vertex("a")
                edgeValue = "a"
            })

            val t = Vertex("t")

            addEdge(directedEdge {
                from = Vertex("e")
                to = t
                edgeValue = "t"
            })

            addEdge(directedEdge {
                from = Vertex("a")
                to = t
                edgeValue = "t"
            })
        }

        val target = Vertex("a")
        val result = T.dfs(target)
        assertIs<DfsResult.Found<Vertex<String>>>(result)
        assertEquals(result.target, target, "Result found from DFS should match target vertex")
    }

    @Test
    fun testDfsOnCycleResultFound() {
        val G = buildDirectedGraph<Int, Int> {
            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(2)
                edgeValue = 12
            })

            addEdge(directedEdge {
                from = Vertex(2)
                to = Vertex(3)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(3)
                to = Vertex(1)
                edgeValue = 4
            })
        }

        val target = Vertex(3)
        val result = G.dfs(target)
        assertIs<DfsResult.Found<Vertex<Int>>>(result)
        assertEquals(result.target, target)
    }

    /**
     * Test the case that CycleDetected is returned on a graph before
     * the target is either found or conclusively not found.
     */
    @Test
    fun testDfsOnCycleResultInconclusive() {
        /* Here is the graph being tested:

                 > 2 --> 4
               /   |
              1    |
               \   |
                 < 3

         */
        val G = buildDirectedGraph<Int, Int> {
            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(2)
                edgeValue = 12
            })

            addEdge(directedEdge {
                from = Vertex(2)
                to = Vertex(3)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(3)
                to = Vertex(1)
                edgeValue = 4
            })

            addEdge(directedEdge {
                from = Vertex(3)
                to = Vertex(4)
                edgeValue = 1
            })
        }

        val target = Vertex(4)
        val result = G.dfs(target)
        assertIs<DfsResult.CycleDetected>(result)
    }

    @Test
    fun testDetectCyclesOnCyclicGraph() {
        val G = buildDirectedGraph<Int, Int> {
            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(2)
                edgeValue = 12
            })

            addEdge(directedEdge {
                from = Vertex(2)
                to = Vertex(3)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(3)
                to = Vertex(1)
                edgeValue = 4
            })
        }

        assertTrue("Cycle graph should report CycleDetected") { G.detectCycles() }
    }

    @Test
    fun testDetectCyclesOnAcyclicGraph() {
        val G = buildDirectedGraph<Int, Int> {
            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(2)
                edgeValue = 12
            })

            addEdge(directedEdge {
                from = Vertex(2)
                to = Vertex(3)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(3)
                edgeValue = 4
            })
        }

        assertFalse("Acyclic graph should not report CycleDetected") { G.detectCycles() }
    }

    @Test
    fun testDfsOnTreeResultNotFound() {
        val G = buildDirectedGraph<Int, Int> {
            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(2)
                edgeValue = 12
            })

            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(3)
                edgeValue = 4
            })

            addEdge(directedEdge {
                from = Vertex(2)
                to = Vertex(4)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(3)
                to = Vertex(5)
                edgeValue = 1
            })
        }

        val target = Vertex(6)
        val result = G.dfs(target)
        assertIs<DfsResult.NotFound>(result)
    }

    @Test
    fun testDepthFirstPathsWithP3() {
        /* Here is the graph being tested:

          1 --> 2 --> 3 --> 4 --> 5

         */
        val G = buildDirectedGraph<Int, Int> {
            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(2)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(2)
                to = Vertex(3)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(3)
                to = Vertex(4)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(4)
                to = Vertex(5)
                edgeValue = 1
            })
        }

        val paths = G.depthFirstPaths()
        assertEquals(1, paths.size, "There should only be 1 path in the graph")
        assertEquals(G.m, paths[0].size, "The path should have as many edges as the graph")
    }

    @Test
    fun testDepthFirstPathsWithTree() {
        /* Here is the tree being tested:
               > 6 --> 7
             /
           1 --> 4 --> 5
             \
               > 2 --> 3
         */
        val G = buildDirectedGraph<Int, Int> {
            addVertex(Vertex(1))
            (2..6 step 2).forEach {
                addEdge(directedEdge {
                    from = Vertex(1)
                    to = Vertex(it)
                    edgeValue = 1
                })

                addEdge(directedEdge {
                    from = Vertex(it)
                    to = Vertex(it + 1)
                    edgeValue = 1
                })
            }
        }

        val paths = G.depthFirstPaths()
        assertEquals(3, paths.size, "There should only 3 paths in the graph")
        paths.forEach {
            assertEquals(2, it.size, "Each path should have 2 edges in it")
        }
    }

    @Test
    fun testDepthFirstPathsWithCycle() {
        /* Here is the graph being tested:

                 > 2 --> 4
               /   |
              1    |
               \   |
                 < 3 --> 5

         */

        val G = buildDirectedGraph<Int, Int> {
            addEdge(directedEdge {
                from = Vertex(1)
                to = Vertex(2)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(2)
                to = Vertex(3)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(3)
                to = Vertex(1)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(2)
                to = Vertex(4)
                edgeValue = 1
            })

            addEdge(directedEdge {
                from = Vertex(3)
                to = Vertex(5)
                edgeValue = 1
            })
        }

        assertFails("Depth-first paths in cyclic graph should fail") {
            G.depthFirstPaths()
        }
    }
}