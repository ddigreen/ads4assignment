# Assignment 4 – Smart City / Smart Campus Scheduling

This repo implements:
- **SCC (Tarjan)** + **condensation DAG**
- **Topological order (Kahn)**
- **Shortest & Longest paths in DAG** (edge-weight model)
- **Metrics**: DFS visits/edges, queue pushes/pops, relaxations; timing via `System.nanoTime()`

## Run
```bash
# from project root
mvn -q -DskipTests exec:java -Dexec.args="data/tasks.json`
```

## Tests
```bash
mvn -q -Dtest=BasicTests test
```

## Data
- Provided: `data/tasks.json`
- Generated: 9 datasets spanning small/medium/large, DAG/mixed/cyclic.

## Weight model
We use the **edge weight** model (as in `tasks.json`). When compressing SCCs, the condensation DAG edge weight equals the **minimum** weight among original edges crossing the two components. This preserves single-source shortest paths while keeping the graph acyclic.

## Structure
```
src/main/java/
  graph/model/Graph.java
  graph/metrics/Metrics.java
  graph/scc/TarjanSCC.java
  graph/topo/KahnTopo.java
  graph/dagsp/DagSP.java
  dagsp/Main.java
src/test/java/test/BasicTests.java
data/*.json
```

## Notes
- Critical path = longest path on the condensation DAG from the source component.
- All algorithms are linear in DAG size; Tarjan is O(n+m).
