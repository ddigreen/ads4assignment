package graph.dagsp;

import graph.model.Graph;
import graph.metrics.Metrics;
import graph.topo.KahnTopo;
import java.util.*;

/** Shortest & longest paths on DAG. Weight model: edge weights. */
public class DagSP {
    private final Graph dag;
    private final Metrics metrics;
    private List<Integer> topo;

    public DagSP(Graph dag, Metrics metrics){
        this.dag = dag;
        this.metrics = metrics;
        this.topo = KahnTopo.order(dag, metrics);
    }

    public Result shortest(int src){
        int n = dag.n;
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Long.MAX_VALUE/4);
        Arrays.fill(parent, -1);
        dist[src] = 0;
        for(int u: topo){
            if(dist[u] == Long.MAX_VALUE/4) continue;
            for(Graph.Edge e: dag.adj.get(u)){
                long nd = dist[u] + e.w;
                if(nd < dist[e.v]){
                    dist[e.v] = nd;
                    parent[e.v] = u;
                    metrics.relaxations++;
                }
            }
        }
        return new Result(dist, parent);
    }

    public Result longest(int src){
        int n = dag.n;
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Long.MIN_VALUE/4);
        Arrays.fill(parent, -1);
        dist[src] = 0;
        for(int u: topo){
            if(dist[u] == Long.MIN_VALUE/4) continue;
            for(Graph.Edge e: dag.adj.get(u)){
                long nd = dist[u] + e.w;
                if(nd > dist[e.v]){
                    dist[e.v] = nd;
                    parent[e.v] = u;
                }
            }
        }
        return new Result(dist, parent);
    }

    public static List<Integer> reconstructPath(int target, int[] parent){
        List<Integer> path = new ArrayList<>();
        int v = target;
        while(v!=-1){ path.add(v); v = parent[v]; }
        Collections.reverse(path);
        return path;
    }

    public static class Result{
        public final long[] dist;
        public final int[] parent;
        public Result(long[] d, int[] p){ this.dist=d; this.parent=p; }
    }
}
