package org.stuygfx.graphics;

import java.util.ArrayList;

public class EdgeMatrix {

    public ArrayList<Edge> edges;

    public EdgeMatrix() {
        edges = new ArrayList<Edge>();
    }

    public void addPoint(Point p) {
        edges.add(new Edge(p, p));
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void addEdge(Point start, Point end) {
        edges.add(new Edge(start, end));
    }
}
