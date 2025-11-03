package graph.model;

import java.util.*;

public class Graph {
    public final int n;
    public final boolean directed;
    public final List<List<Edge>> adj;
    public static class Edge {
        public final int u, v;
        public final int w;
        public Edge(int u, int v, int w) { this.u=u; this.v=v; this.w=w; }
    }
    public Graph(int n, boolean directed){
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>();
        for(int i=0;i<n;i++) adj.add(new ArrayList<>());
    }
    public void addEdge(int u, int v, int w){
        adj.get(u).add(new Edge(u,v,w));
        if(!directed) adj.get(v).add(new Edge(v,u,w));
    }
    public List<Edge> edges(){ // convenience
        List<Edge> e = new ArrayList<>();
        for(List<Edge> lst: adj) e.addAll(lst);
        return e;
    }
}
