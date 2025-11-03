# REPORT

Generated: 2025-11-03T09:11:12.488193Z

We use the **edge weight** model. SCCs are compressed, condensation edges carry the *minimum* cross-edge weight.

## Data summary

- n = 8, edges = 7, directed = True, source = 4

## SCC result

- Component 0: [1, 2, 3] (size=3)

- Component 1: [0] (size=1)

- Component 2: [7] (size=1)

- Component 3: [6] (size=1)

- Component 4: [5] (size=1)

- Component 5: [4] (size=1)

- Total components: 6

## Topological order of condensation DAG

`1 -> 5 -> 0 -> 4 -> 3 -> 2`

## DAG Shortest distances from source component

```
[1000000000000000, 1000000000000000, 8, 7, 2, 0]
```

## DAG Longest distances (critical) from source component

```
[-1000000000000000, -1000000000000000, 8, 7, 2, 0]
```

- Critical length: **8**

- Critical path (components): `[5, 4, 3, 2]`

## Metrics

- SCC: dfsVisits=8, dfsEdges=7

- Topo/SP: queuePushes=6, queuePops=6, relaxations=3
