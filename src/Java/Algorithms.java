import java.util.Map;

/**
 * Implement the six methods and add your route-reconstruction helper.
 * Input extraction is provided; all algorithm state belongs to the current request.
 * See ../../README.md for contracts and Java guidance.
 */
public final class Algorithms {
    /** DSU with path compression and union by size/rank; return states. */

    static Map<String, Object> dsu(Map<String, Object> input) {
        int n = Input.vertexCount(input);
        int[][] operations = Input.operations(input);

        int[] parent = new int[n];
        int[] size = new int[n];
        int components = n;
        int largest = n == 0 ? 0 : 1;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        java.util.List<int[]> states = new java.util.ArrayList<>();

        for (int[] operation : operations) {
            int u = operation[0];
            int v = operation[1];

            int rootU = find(parent, u);
            int rootV = find(parent, v);

            if (rootU != rootV) {
                if (size[rootU] < size[rootV]) {
                    int temp = rootU;
                    rootU = rootV;
                    rootV = temp;
                }

                parent[rootV] = rootU;
                size[rootU] += size[rootV];
                components--;

                largest = Math.max(largest, size[rootU]);
            }

            states.add(new int[]{components, largest});
        }

        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("states", states);
        return result;
    }

    private static int find(int[] parent, int vertex) {
        while (parent[vertex] != vertex) {
            parent[vertex] = parent[parent[vertex]];
            vertex = parent[vertex];
        }
        return vertex;
    }
        private static int countComponents(int[] parent, int n) {
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (find(parent, i) == i) {
                count++;
            }
        }

        return count;
    }


    /** Separate Kruskal implementation; return cost, edge_ids, components. */
    

static Map<String, Object> kruskal(Map<String, Object> input) {
    int n = Input.vertexCount(input);
    int[][] edges = Input.edges(input);

    java.util.List<Integer> order = new java.util.ArrayList<>();

    for (int i = 0; i < edges.length; i++) {
        order.add(i);
    }

    order.sort((a, b) -> {
        int cmp = Integer.compare(edges[a][2], edges[b][2]);
        if (cmp != 0) {
            return cmp;
        }
        return Integer.compare(a, b);
    });

    int[] parent = new int[n];
    int[] size = new int[n];

    for (int i = 0; i < n; i++) {
        parent[i] = i;
        size[i] = 1;
    }

    java.util.List<Integer> edgeIds = new java.util.ArrayList<>();
    long cost = 0;
    int components = n;

    for (int id : order) {
        int u = edges[id][0];
        int v = edges[id][1];
        int weight = edges[id][2];

        int rootU = find(parent, u);
        int rootV = find(parent, v);

        if (rootU == rootV) {
            continue;
        }

        if (size[rootU] < size[rootV]) {
            int temp = rootU;
            rootU = rootV;
            rootV = temp;
        }

        parent[rootV] = rootU;
        size[rootU] += size[rootV];

        edgeIds.add(id);
        cost += weight;
        components--;
    }

    Map<String, Object> result = new java.util.LinkedHashMap<>();
    result.put("cost", cost);
    result.put("edge_ids", edgeIds);
    result.put("components", components);

    return result;
}


    /** Separate Prim implementation; restart for every unvisited component. */
    
static Map<String, Object> prim(Map<String, Object> input) {
    int n = Input.vertexCount(input);
    int[][] edges = Input.edges(input);

    java.util.List<java.util.List<int[]>> graph =
            new java.util.ArrayList<>();

    for (int i = 0; i < n; i++) {
        graph.add(new java.util.ArrayList<>());
    }

    for (int id = 0; id < edges.length; id++) {
        int u = edges[id][0];
        int v = edges[id][1];
        int w = edges[id][2];

        if (u == v) {
            continue;
        }

        graph.get(u).add(new int[]{v, w, id});
        graph.get(v).add(new int[]{u, w, id});
    }

    boolean[] visited = new boolean[n];
    java.util.List<Integer> edgeIds = new java.util.ArrayList<>();
    long cost = 0;
    int components = 0;

    java.util.PriorityQueue<int[]> pq =
            new java.util.PriorityQueue<>((a, b) -> {
                int cmp = Integer.compare(a[0], b[0]);
                if (cmp != 0) return cmp;
                return Integer.compare(a[1], b[1]);
            });

    for (int start = 0; start < n; start++) {
        if (visited[start]) {
            continue;
        }

        components++;
        visited[start] = true;

        for (int[] edge : graph.get(start)) {
            pq.offer(new int[]{edge[1], edge[2], edge[0]});
        }

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int weight = current[0];
            int id = current[1];
            int vertex = current[2];

            if (visited[vertex]) {
                continue;
            }

            visited[vertex] = true;
            edgeIds.add(id);
            cost += weight;

            for (int[] edge : graph.get(vertex)) {
                if (!visited[edge[0]]) {
                    pq.offer(new int[]{edge[1], edge[2], edge[0]});
                }
            }
        }
    }

    Map<String, Object> result = new java.util.LinkedHashMap<>();
    result.put("cost", cost);
    result.put("edge_ids", edgeIds);
    result.put("components", components);

    return result;
}

static Map<String, Object> scc(Map<String, Object> input) {
    int n = Input.vertexCount(input);
    int[][] edges = Input.edges(input);

    java.util.List<java.util.List<Integer>> graph =
            new java.util.ArrayList<>();
    java.util.List<java.util.List<Integer>> reverse =
            new java.util.ArrayList<>();

    for (int i = 0; i < n; i++) {
        graph.add(new java.util.ArrayList<>());
        reverse.add(new java.util.ArrayList<>());
    }

    for (int[] edge : edges) {
        int u = edge[0];
        int v = edge[1];
        graph.get(u).add(v);
        reverse.get(v).add(u);
    }

    boolean[] visited = new boolean[n];
    java.util.List<Integer> order = new java.util.ArrayList<>();

    for (int start = 0; start < n; start++) {
        if (visited[start]) continue;

        java.util.ArrayDeque<int[]> stack = new java.util.ArrayDeque<>();
        stack.push(new int[]{start, 0});
        visited[start] = true;

        while (!stack.isEmpty()) {
            int[] top = stack.peek();
            int v = top[0];
            int next = top[1];

            if (next < graph.get(v).size()) {
                int to = graph.get(v).get(next);
                top[1]++;

                if (!visited[to]) {
                    visited[to] = true;
                    stack.push(new int[]{to, 0});
                }
            } else {
                order.add(v);
                stack.pop();
            }
        }
    }

    int[] component = new int[n];
    java.util.Arrays.fill(component, -1);
    int componentCount = 0;

    for (int i = order.size() - 1; i >= 0; i--) {
        int start = order.get(i);
        if (component[start] != -1) continue;

        java.util.ArrayDeque<Integer> stack = new java.util.ArrayDeque<>();
        stack.push(start);
        component[start] = componentCount;

        while (!stack.isEmpty()) {
            int v = stack.pop();

            for (int to : reverse.get(v)) {
                if (component[to] == -1) {
                    component[to] = componentCount;
                    stack.push(to);
                }
            }
        }

        componentCount++;
    }

    java.util.List<Integer> componentList = new java.util.ArrayList<>();
    for (int c : component) {
        componentList.add(c);
    }

    java.util.List<java.util.List<Integer>> condensation =
            new java.util.ArrayList<>();
    java.util.Set<Long> seen = new java.util.HashSet<>();

    for (int[] edge : edges) {
        int a = component[edge[0]];
        int b = component[edge[1]];

        if (a != b) {
            long key = ((long) a << 32) | (b & 0xffffffffL);
            if (seen.add(key)) {
                condensation.add(java.util.Arrays.asList(a, b));
            }
        }
    }

    Map<String, Object> result = new java.util.LinkedHashMap<>();
    result.put("component", componentList);
    result.put("condensation", condensation);
    return result;
}


    /** Three-state DFS and reverse finish order; return acyclic and order. */
    
static Map<String, Object> topo(Map<String, Object> input) {
    int n = Input.vertexCount(input);
    int[][] edges = Input.edges(input);

    java.util.List<java.util.List<Integer>> graph =
            new java.util.ArrayList<>();

    for (int i = 0; i < n; i++) {
        graph.add(new java.util.ArrayList<>());
    }

    for (int[] edge : edges) {
        graph.get(edge[0]).add(edge[1]);
    }

    int[] state = new int[n];
    java.util.List<Integer> order = new java.util.ArrayList<>();

    for (int start = 0; start < n; start++) {
        if (state[start] != 0) {
            continue;
        }

        java.util.ArrayDeque<int[]> stack = new java.util.ArrayDeque<>();
        stack.push(new int[]{start, 0});
        state[start] = 1;

        while (!stack.isEmpty()) {
            int[] top = stack.peek();
            int vertex = top[0];
            int next = top[1];

            if (next < graph.get(vertex).size()) {
                int to = graph.get(vertex).get(next);
                top[1]++;

                if (state[to] == 1) {
                    Map<String, Object> result =
                            new java.util.LinkedHashMap<>();
                    result.put("acyclic", false);
                    result.put("order", new java.util.ArrayList<Integer>());
                    return result;
                }

                if (state[to] == 0) {
                    state[to] = 1;
                    stack.push(new int[]{to, 0});
                }
            } else {
                state[vertex] = 2;
                order.add(vertex);
                stack.pop();
            }
        }
    }

    java.util.Collections.reverse(order);

    Map<String, Object> result = new java.util.LinkedHashMap<>();
    result.put("acyclic", true);
    result.put("order", order);
    return result;
}


    /** Whole-graph cycle check and DAG relaxation; return acyclic, distance, parent_edge. */
    
static Map<String, Object> dag(Map<String, Object> input) {
    int n = Input.vertexCount(input);
    int source = Input.source(input);
    int[][] edges = Input.edges(input);

    java.util.List<java.util.List<int[]>> graph =
            new java.util.ArrayList<>();

    for (int i = 0; i < n; i++) {
        graph.add(new java.util.ArrayList<>());
    }

    for (int id = 0; id < edges.length; id++) {
        int u = edges[id][0];
        int v = edges[id][1];
        int weight = edges[id][2];

        graph.get(u).add(new int[]{v, weight, id});
    }

    int[] state = new int[n];
    java.util.List<Integer> order = new java.util.ArrayList<>();

    for (int start = 0; start < n; start++) {
        if (state[start] != 0) {
            continue;
        }

        java.util.ArrayDeque<int[]> stack = new java.util.ArrayDeque<>();
        stack.push(new int[]{start, 0});
        state[start] = 1;

        while (!stack.isEmpty()) {
            int[] top = stack.peek();
            int vertex = top[0];
            int next = top[1];

            if (next < graph.get(vertex).size()) {
                int to = graph.get(vertex).get(next)[0];
                top[1]++;

                if (state[to] == 1) {
                    Map<String, Object> result =
                            new java.util.LinkedHashMap<>();
                    result.put("acyclic", false);
                    result.put("distance", new java.util.ArrayList<>());
                    result.put("parent_edge", new java.util.ArrayList<>());
                    return result;
                }

                if (state[to] == 0) {
                    state[to] = 1;
                    stack.push(new int[]{to, 0});
                }
            } else {
                state[vertex] = 2;
                order.add(vertex);
                stack.pop();
            }
        }
    }

    java.util.Collections.reverse(order);

    long[] distance = new long[n];
    boolean[] reachable = new boolean[n];
    int[] parentEdge = new int[n];

    java.util.Arrays.fill(parentEdge, -1);
    reachable[source] = true;
    distance[source] = 0;

    for (int vertex : order) {
        if (!reachable[vertex]) {
            continue;
        }

        for (int[] edge : graph.get(vertex)) {
            int to = edge[0];
            int weight = edge[1];
            int id = edge[2];

            long candidate = distance[vertex] + weight;

            if (!reachable[to] || candidate < distance[to]) {
                reachable[to] = true;
                distance[to] = candidate;
                parentEdge[to] = id;
            }
        }
    }

    java.util.List<Object> distances = new java.util.ArrayList<>();
    java.util.List<Integer> parents = new java.util.ArrayList<>();

    for (int i = 0; i < n; i++) {
        if (reachable[i]) {
            distances.add(distance[i]);
        } else {
            distances.add(null);
        }
        parents.add(parentEdge[i]);
    }

    Map<String, Object> result = new java.util.LinkedHashMap<>();
    result.put("acyclic", true);
    result.put("distance", distances);
    result.put("parent_edge", parents);
    return result;
}
    /**
     * Reconstruct a route from source to target using parent edge IDs.
     * Returns an empty list if the target is unreachable.
     */
    static java.util.List<Integer> reconstructRoute(
            int source,
            int target,
            int[][] edges,
            int[] parentEdge) {

        java.util.List<Integer> route = new java.util.ArrayList<>();

        if (source == target) {
            route.add(source);
            return route;
        }

        java.util.ArrayList<Integer> reversed = new java.util.ArrayList<>();
        int current = target;

        while (current != source) {
            if (current < 0 || current >= parentEdge.length) {
                return route;
            }

            int edgeId = parentEdge[current];

            if (edgeId < 0 || edgeId >= edges.length) {
                return route;
            }

            int from = edges[edgeId][0];
            int to = edges[edgeId][1];

            if (to != current) {
                return route;
            }

            reversed.add(current);
            current = from;

            if (reversed.size() > parentEdge.length) {
                return route;
            }
        }

        reversed.add(source);

        for (int i = reversed.size() - 1; i >= 0; i--) {
            route.add(reversed.get(i));
        }

        return route;
    }

} 
 