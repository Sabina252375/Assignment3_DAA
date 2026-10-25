# Graph Algorithms — Analysis

## DSU
DSU manages connected components. I used path compression and union by size to make operations faster.
After each operation, it returns the number of components and the size of the largest component.
Correctness: When two different sets are merged, the component count decreases and their sizes are combined.
Time complexity: (O(\alpha(V))) amortized per operation.
Space complexity: (O(V)).
## Kruskal
Kruskal sorts edges by weight and uses DSU to avoid cycles. It adds an edge only if its endpoints belong to different components. It also works with disconnected graphs.
Correctness: Processing edges in increasing weight order produces a minimum spanning forest.
Time complexity: (O(E \log E)).
Space complexity: (O(V + E)).
## Prim
Prim selects the cheapest edge connecting a visited vertex to an unvisited vertex. For disconnected graphs, it starts a new component when needed.
Correctness: Each selected edge connects different visited and unvisited vertices, so it does not create a cycle. The algorithm builds a minimum spanning tree for each component.
Time complexity: (O(E \log V)).
Space complexity: (O(V + E)).
## SCC
I used Kosaraju's algorithm. It runs DFS on the original graph, reverses the edges, and runs DFS again in reverse finishing order.
Correctness: The two DFS passes identify strongly connected components and assign labels to their vertices.
Time complexity: (O(V + E)).
Space complexity: (O(V + E)).
## Topological Sort
DFS uses three states: unvisited, visiting, and visited. If it reaches a visiting vertex, the graph contains a cycle. Otherwise, reverse finishing order gives a topological order.
If a cycle exists, the algorithm returns acyclic: false and an empty order.
Time complexity: (O(V + E)).
Space complexity: (O(V + E)).
## DAG Shortest Paths
The algorithm processes vertices in topological order and updates distances. Negative edge weights are supported. Unreachable vertices have null distances.
If the graph contains a cycle, the algorithm returns acyclic: false and empty result arrays.
Time complexity: (O(V + E)).
Space complexity: (O(V + E)).
## Route Reconstruction
The route helper follows parent edge IDs backwards from the target and reconstructs the path in the correct order.
I tested four cases:
A reachable target.
Source equals target.
An unreachable target.
A route using a parallel edge.
All four route tests passed.
## Boundary cases and State Reset
The tests cover disconnected graphs, cycles, unreachable vertices, negative weights in DAG shortest paths, deep graph chains, and multiple requests in one process.
The program resets its state between requests so that previous results do not affect new requests. 
## Testing
I ran the public tests:

`python3.14 tools/run.py --check`

All 36 public tests passed.

I also tested my own inputs with:

`python3.14 tools/run.py --input tests/my_cases.jsonl`

The route helper tests checked a normal route, source equals target, an unreachable target, and a parallel edge. All four passed.

## Benchmark
I tested Kruskal and Prim on the same randomly generated graphs. I used Java 17 and seed 42. Each algorithm ran three times, and I recorded the median time.
Kruskal was faster in these tests, especially on larger graphs. These results may vary depending on the computer and system load. The benchmark used only three runs per algorithm and size.
| Vertices | Edges | Kruskal (ms) | Prim (ms) |
|----------|-------|--------------|-----------|
| 500      | 2000  | 1.223        | 1.245     |
| 1000     | 4000  | 1.075        | 1.734     |
| 2000     | 8000  | 2.464        | 3.171     |


## Sources
I used the assignment instructions and README. AI assistance helped me understand the algorithms and prepare this report. I checked the implementation using the public and custom tests.
