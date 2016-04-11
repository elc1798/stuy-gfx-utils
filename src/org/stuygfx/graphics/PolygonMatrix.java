package org.stuygfx.graphics;

import java.util.ArrayList;

public class PolygonMatrix {

    public ArrayList<Triangle> polygons;

    public PolygonMatrix() {
        polygons = new ArrayList<Triangle>();
    }

    public void addTriangle(Point p1, Point p2, Point p3) {
        polygons.add(new Triangle(p1, p2, p3));
    }

    public void addRectPrism(Integer x, Integer y, Integer z, Integer dx, Integer dy, Integer dz) {
        /*
         *     P7_______P8
         *      /     /|
         *     /     / |
         *  P4/_____/P3|
         *    |     |  / P5
         *    |     | /
         *    |_____|/
         *  P1       P2
         *
         * Note: I denote faces in "Rubik's Cube" format
         */
        Point P1 = new Point(x, y, z);
        Point P2 = new Point(x + dx, y, z);
        Point P3 = new Point(x + dx, y + dy, z);
        Point P4 = new Point(x, y + dy, z);
        Point P5 = new Point(x + dx, y, z + dz);
        Point P6 = new Point(x, y, z + dz);
        Point P7 = new Point(x, y + dy, z + dz);
        Point P8 = new Point(x + dx, y + dy, z + dz);
        // Front
        addTriangle(P1, P2, P3);
        addTriangle(P3, P4, P1);
        // Up
        addTriangle(P4, P3, P8);
        addTriangle(P8, P7, P4);
        // Right
        addTriangle(P2, P5, P8);
        addTriangle(P8, P3, P2);
        // Left
        addTriangle(P6, P1, P4);
        addTriangle(P4, P7, P6);
        // Down
        addTriangle(P6, P5, P2);
        addTriangle(P2, P1, P6);
        // Back
        addTriangle(P5, P6, P7);
        addTriangle(P7, P8, P5);
    }

    public void addSphere(Double cx, Double cy, Double cz, Double radius, Integer res) {
        EdgeMatrix spherePoints = new EdgeMatrix();
        spherePoints.addSphere(cx, cy, cz, radius);
        Edge[] points = (Edge[]) spherePoints.edges.toArray();
        for (int i = 0; i < points.length; i += res + 1) {
            for (int j = 0; j < res; j++) {
                addTriangle(points[(i + j) % points.length].start,
                    points[(i + res + 1 + ((j + 1) % (res + 1))) % points.length].start,
                    points[(i + j + res + 1) % points.length].start
                    );
                addTriangle(points[(i + j) % points.length].start,
                    points[(i + ((j + 1) % (res + 1))) % points.length].start,
                    points[(i + res + 1 + ((j + 1) % (res + 1))) % points.length].start);
            }
        }
    }
}
