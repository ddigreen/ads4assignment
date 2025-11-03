package test;

import graph.model.Graph;
import graph.metrics.Metrics;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopo;
import graph.dagsp.DagSP;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BasicTests {

    @Test
    public void sccSimpleCycle(){
        Graph g = new Graph(3, true);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        Metrics m = new Metrics();
        TarjanSCC t = new TarjanSCC(g,m);
        int[] comp = t.run();
        int c0 = comp[0];
        assertEquals(c0, comp[1]);
        assertEquals(c0, comp[2]);
        assertEquals(1, t.buildCondensation().n);
    }

    @Test
    public void topoLine(){
        Graph dag = new Graph(4, true);
        dag.addEdge(0,1,1);
        dag.addEdge(1,2,1);
        dag.addEdge(2,3,1);
        Metrics m = new Metrics();
        var order = KahnTopo.order(dag, m);
        assertEquals(Arrays.asList(0,1,2,3), order);
    }

    @Test
    public void dagSPShortestLongest(){
        Graph dag = new Graph(4, true);
        dag.addEdge(0,1,2);
        dag.addEdge(0,2,1);
        dag.addEdge(2,3,5);
        dag.addEdge(1,3,2);
        Metrics m = new Metrics();
        DagSP sp = new DagSP(dag, m);
        var rS = sp.shortest(0);
        var rL = sp.longest(0);
        assertEquals(4, rS.dist[3]); // 0->1(2)->3(2)
        assertEquals(6, rL.dist[3]); // 0->2(1)->3(5)
    }
}
