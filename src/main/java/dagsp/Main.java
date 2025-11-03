package dagsp;

import graph.model.Graph;
import graph.metrics.Metrics;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopo;
import graph.dagsp.DagSP;
import java.nio.file.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.type.TypeReference;

/** Main entry: reads JSON, runs SCC, builds condensation DAG, runs topo + DAG SP (shortest & longest). */
public class Main {
    static class Input {
        public boolean directed;
        public int n;
        public List<Map<String,Integer>> edges;
        public Integer source;
        public String weight_model;
    }

    public static void main(String[] args) throws Exception {
        if(args.length==0){
            System.err.println("Usage: java -jar assignmentDAA4.jar data/tasks.json");
            System.exit(1);
        }
        Path path = Paths.get(args[0]);
        ObjectMapper om = new ObjectMapper();
        Input in = om.readValue(Files.readAllBytes(path), Input.class);

        Graph g = new Graph(in.n, in.directed);
        for(var e: in.edges){
            int u = e.get("u"), v = e.get("v");
            int w = e.getOrDefault("w", 1);
            g.addEdge(u, v, w);
        }
        int source = in.source==null?0:in.source;
        Metrics m1 = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(g, m1);
        int[] comp = tarjan.run();
        int compCount = 0;
        for(int x: comp) compCount = Math.max(compCount, x);
        compCount += 1;
        Graph dag = tarjan.buildCondensation();

        // Edge-weight model aggregation: min edge weight between components
        int C = dag.n;
        long[][] best = new long[C][C];
        for(int i=0;i<C;i++) Arrays.fill(best[i], Long.MAX_VALUE/4);
        for(var e: g.edges()){
            int cu = comp[e.u], cv = comp[e.v];
            if(cu!=cv){
                best[cu][cv] = Math.min(best[cu][cv], e.w);
            }
        }
        // overwrite dag weights with aggregated weights
        for(int u=0; u<dag.n; u++){
            for(int i=0;i<dag.adj.get(u).size(); i++){
                var e = dag.adj.get(u).get(i);
                int v = e.v;
                int w = (int)best[u][v];
                dag.adj.get(u).set(i, new Graph.Edge(u, v, w));
            }
        }

        Metrics m2 = new Metrics();
        List<Integer> order = KahnTopo.order(dag, m2);
        DagSP sp = new DagSP(dag, m2);
        int srcComp = comp[source];
        var resShort = sp.shortest(srcComp);
        var resLong = sp.longest(srcComp);

        // Pretty print
        System.out.println("=== SCC Groups ===");
        Map<Integer, List<Integer>> groups = new HashMap<>();
        for(int v=0; v<comp.length; v++){
            groups.computeIfAbsent(comp[v], k -> new ArrayList<>()).add(v);
        }
        for(var e: groups.entrySet()){
            System.out.println("Component " + e.getKey() + " -> " + e.getValue());
        }
        System.out.println("Total components: " + groups.size());

        System.out.println("\n=== Condensation DAG Topo Order ===");
        System.out.println(order);

        System.out.println("\n=== DAG Shortest Distances from component " + srcComp + " ===");
        System.out.println(Arrays.toString(resShort.dist));

        System.out.println("\n=== DAG Longest Distances from component " + srcComp + " (critical path lengths) ===");
        System.out.println(Arrays.toString(resLong.dist));

        // Find critical target (max distance)
        long bestLen = Long.MIN_VALUE/4; int bestV = -1;
        for(int v=0; v<resLong.dist.length; v++){
            if(resLong.dist[v] > bestLen){
                bestLen = resLong.dist[v]; bestV = v;
            }
        }
        System.out.println("Critical length: " + bestLen);
        System.out.println("Critical path (components): " + DagSP.reconstructPath(bestV, resLong.parent));

        // Metrics
        System.out.println("\n=== Metrics (SCC) ===");
        System.out.println("timeNs=" + m1.timeNs + " dfsVisits=" + m1.dfsVisits + " dfsEdges=" + m1.dfsEdges);
        System.out.println("=== Metrics (Topo + DAG-SP) ===");
        System.out.println("queuePushes=" + m2.queuePushes + " queuePops=" + m2.queuePops + " relaxations=" + m2.relaxations);
    }
}
