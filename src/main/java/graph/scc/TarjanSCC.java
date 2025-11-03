package graph.scc;

import graph.model.Graph;
import graph.metrics.Metrics;
import java.util.*;

/** Tarjan SCC algorithm (O(n+m)). */
public class TarjanSCC {
    private final Graph g;
    private final Metrics metrics;
    private int timer = 0, compCnt = 0;
    private final int[] tin, low, comp;
    private final boolean[] inStack;
    private final Deque<Integer> st = new ArrayDeque<>();

    public TarjanSCC(Graph g, Metrics metrics){
        this.g = g;
        this.metrics = metrics;
        this.tin = new int[g.n];
        this.low = new int[g.n];
        this.comp = new int[g.n];
        Arrays.fill(tin, -1);
        Arrays.fill(low, -1);
        Arrays.fill(comp, -1);
        this.inStack = new boolean[g.n];
    }

    public int[] run(){
        long t0 = System.nanoTime();
        for(int v=0; v<g.n; v++){
            if(tin[v] == -1) dfs(v);
        }
        metrics.timeNs += System.nanoTime() - t0;
        return comp;
    }

    private void dfs(int v){
        metrics.dfsVisits++;
        tin[v] = low[v] = timer++;
        st.push(v);
        inStack[v] = true;

        for(Graph.Edge e : g.adj.get(v)){
            metrics.dfsEdges++;
            int to = e.v;
            if(tin[to] == -1){
                dfs(to);
                low[v] = Math.min(low[v], low[to]);
            } else if(inStack[to]){
                low[v] = Math.min(low[v], tin[to]);
            }
        }
        if(low[v] == tin[v]){ // root of SCC
            while(true){
                int u = st.pop();
                inStack[u] = false;
                comp[u] = compCnt;
                if(u == v) break;
            }
            compCnt++;
        }
    }

    public int components(){
        return compCnt;
    }

    /** Build condensation DAG: nodes are components 0..compCnt-1 */
    public Graph buildCondensation(){
        Graph dag = new Graph(compCnt, true);
        Set<Long> seen = new HashSet<>();
        for(int u=0; u<g.n; u++){
            for(Graph.Edge e: g.adj.get(u)){
                int cu = comp[u], cv = comp[e.v];
                if(cu != cv){
                    long key = (((long)cu)<<32) ^ (cv & 0xffffffffL);
                    if(!seen.contains(key)){
                        seen.add(key);
                        // weight 0 here; real edge weights handled in SP layer via aggregation
                        dag.addEdge(cu, cv, 0);
                    }
                }
            }
        }
        return dag;
    }

    /** Map from component -> list of original vertices */
    public Map<Integer, List<Integer>> groups(){
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int v=0; v<comp.length; v++){
            map.computeIfAbsent(comp[v], k -> new ArrayList<>()).add(v);
        }
        return map;
    }
}
