package graph.topo;

import graph.model.Graph;
import graph.metrics.Metrics;
import java.util.*;

public class KahnTopo {
    public static List<Integer> order(Graph dag, Metrics metrics){
        int n = dag.n;
        int[] indeg = new int[n];
        for(int u=0; u<n; u++){
            for(var e: dag.adj.get(u)) indeg[e.v]++;
        }
        Deque<Integer> q = new ArrayDeque<>();
        for(int i=0;i<n;i++) if(indeg[i]==0){ q.add(i); metrics.queuePushes++; }
        List<Integer> res = new ArrayList<>();
        while(!q.isEmpty()){
            int u = q.remove();
            metrics.queuePops++;
            res.add(u);
            for(var e: dag.adj.get(u)){
                if(--indeg[e.v]==0){ q.add(e.v); metrics.queuePushes++; }
            }
        }
        if(res.size()!=n) throw new IllegalStateException("Graph is not a DAG");
        return res;
    }
}
