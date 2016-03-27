package org.stuygfx.graphics;

public class Edge {

    public Point start;
    public Point end;

    public Edge(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Edge(int x1, int y1, int x2, int y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }
}
