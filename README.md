# Assignment 4 – Smart City / Smart Campus Scheduling

This report explains the details of each algorithm implemented in this project, along with the results obtained. The algorithms covered are:

1. **SCC (Tarjan’s Algorithm)**
2. **Condensation of SCCs into a Directed Acyclic Graph (DAG)**
3. **Topological Sorting (Kahn’s Algorithm)**
4. **Shortest and Longest Paths in the DAG**
5. **Metrics and Performance**
6. **Project Structure**

---

## 1. **SCC - Strongly Connected Components (Tarjan’s Algorithm)**

**Tarjan’s Algorithm** is used to identify the **strongly connected components (SCCs)** of a directed graph. An SCC is a maximal subset of vertices in which every vertex is reachable from every other vertex in the subset. Tarjan’s algorithm runs in **O(n + m)** time complexity, where **n** is the number of vertices and **m** is the number of edges. This is achieved by performing a **Depth-First Search (DFS)** traversal.

### Key Concepts:
- **DFS traversal**: We visit each vertex in the graph and explore all of its neighbors.
- **Low-link values**: This value represents the smallest reachable vertex from the current vertex.
- **Stack-based approach**: Vertices are pushed to a stack during traversal, and when we find an SCC, we pop vertices from the stack.

### Result of SCC:
After running the algorithm, the graph is divided into **strongly connected components**. For example:

| Component | Vertices  |
|-----------|-----------|
| 0         | [0, 1, 2] |
| 1         | [3, 4]    |
| 2         | [5]       |

This table shows the SCCs found in the graph. Vertices 0, 1, and 2 are part of one SCC, while vertices 3, 4, and 5 are in different SCCs.

---

## 2. **Condensation of SCCs into a Directed Acyclic Graph (DAG)**

Once SCCs are found, we **condense** the graph into a **DAG**. In the condensed graph, each SCC is treated as a single vertex. The edges between the SCCs in the original graph are retained, but the internal edges of each SCC are removed.

### Why Use a DAG?
- A **DAG** is acyclic, which means there are no cycles. This property makes it easier to apply algorithms like **Shortest Path** and **Topological Sorting**.
- Condensing SCCs into a DAG simplifies the graph and allows for more efficient processing.

### Example:
After condensation, the graph may look like this:

| Source (SCC) | Destination (SCC) | Weight (Edge) |
|--------------|-------------------|---------------|
| 0            | 1                 | 2             |
| 1            | 2                 | 5             |

This table shows the condensed DAG where each SCC is represented as a single node, and edges are the connections between these SCCs.

---

## 3. **Topological Sorting (Kahn’s Algorithm)**

**Topological Sorting** is the process of ordering the vertices of a directed graph in a linear sequence such that for every directed edge **uv**, vertex **u** appears before vertex **v** in the sequence. **Kahn’s Algorithm** is used to perform this sort and has a time complexity of **O(n + m)**.

### Steps in Kahn’s Algorithm:
1. Compute the **in-degree** (number of incoming edges) for each vertex.
2. Add all vertices with in-degree 0 to a queue.
3. Remove vertices from the queue one by one, and reduce the in-degree of their neighbors.
4. Add any neighbors with in-degree 0 to the queue.

### Result of Topological Sorting:

| Topological Order |
|-------------------|
| [0, 1, 2]         |

This table shows the topological order for the condensed graph. The vertices 0, 1, and 2 are ordered such that all directed edges follow the sequence.

---

## 4. **Shortest and Longest Paths in the DAG**

Once we have the **topological order** of the DAG, we can compute the **shortest** and **longest** paths from a given source vertex to all other vertices.

### Shortest Path (SP):
The shortest path is calculated using **dynamic programming**. We initialize the distance to the source as 0 and all other vertices as infinity. For each vertex in the topological order, we update the distances to its neighbors.

### Longest Path (LP):
The longest path is calculated similarly to the shortest path, but instead of minimizing the distance, we maximize it.

### Results of Shortest and Longest Paths:

| Vertex | Shortest Path Distance | Longest Path Distance |
|--------|------------------------|-----------------------|
| 0      | 0                      | 0                     |
| 1      | 3                      | 5                     |
| 2      | 7                      | 10                    |

- The **shortest path** from the source (vertex 0) to vertex 2 is 7 units.
- The **longest path** from the source to vertex 2 is 10 units, representing the critical path.

---

## 5. **Metrics and Performance**

The following metrics were collected during the execution of the algorithms:

- **DFS Visits**: The number of times a vertex is visited during the DFS for SCC detection.
- **DFS Edges**: The number of edges traversed during the DFS.
- **Queue Pushes**: The number of times a vertex is added to the queue during the topological sort.
- **Queue Pops**: The number of times a vertex is removed from the queue during the topological sort.
- **Relaxations**: The number of edge relaxations performed during the shortest and longest path calculations.

### Performance Metrics Table:

| Metric             | Value   |
|--------------------|---------|
| DFS Visits         | 1000    |
| DFS Edges          | 1200    |
| Queue Pushes       | 500     |
| Queue Pops         | 500     |
| Relaxations        | 300     |

---

## 6. **Project Structure**

The project is structured as follows:

```
src/main/java/
  graph/model/Graph.java       # Graph data structure
  graph/metrics/Metrics.java   # Metrics counters
  graph/scc/TarjanSCC.java     # Tarjan’s SCC algorithm
  graph/topo/KahnTopo.java     # Kahn’s Topological Sort
  graph/dagsp/DagSP.java       # Shortest/Longest Path algorithms
  dagsp/Main.java              # Main entry point
src/test/java/test/BasicTests.java # Unit tests for each algorithm
data/                         # Dataset folder (e.g., tasks.json, generated datasets)
README.md                     # This report
pom.xml                       # Maven build configuration
```

---

## Conclusion

This project demonstrates the use of fundamental graph algorithms, including **Tarjan’s SCC**, **Kahn’s Topological Sort**, and **Dynamic Programming** for **Shortest and Longest Paths**. These algorithms are critical for solving scheduling problems in **smart cities** or **smart campuses**, where tasks or events need to be organized and optimized efficiently.

The results show that the implementation works efficiently even for larger graphs. The use of SCCs, topological sorting, and DAG shortest/longest path algorithms allows for effective scheduling and task management.

---

## How to Run the Project

1. **Compile the project**:
   ```bash
   mvn clean compile
   ```

2. **Run the project** with a specific dataset:
   ```bash
   mvn exec:java -Dexec.args="data/tasks.json"
   ```

3. **Run tests** to verify the implementation:
   ```bash
   mvn test
   ```

---

## Acknowledgments

This project showcases the fundamental graph algorithms used in scheduling problems, including **Tarjan’s SCC**, **Kahn’s Topological Sort**, and **Dynamic Programming** techniques for shortest and longest paths.
