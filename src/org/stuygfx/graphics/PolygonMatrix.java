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
        addTriangle(P1.clone(), P2.clone(), P3.clone());
        addTriangle(P3.clone(), P4.clone(), P1.clone());
        // Up
        addTriangle(P4.clone(), P3.clone(), P8.clone());
        addTriangle(P8.clone(), P7.clone(), P4.clone());
        // Right
        addTriangle(P2.clone(), P5.clone(), P8.clone());
        addTriangle(P8.clone(), P3.clone(), P2.clone());
        // Left
        addTriangle(P6.clone(), P1.clone(), P4.clone());
        addTriangle(P4.clone(), P7.clone(), P6.clone());
        // Down
        addTriangle(P6.clone(), P5.clone(), P2.clone());
        addTriangle(P2.clone(), P1.clone(), P6.clone());
        // Back
        addTriangle(P5.clone(), P6.clone(), P7.clone());
        addTriangle(P7.clone(), P8.clone(), P5.clone());
    }

    public void addSphere(Double cx, Double cy, Double cz, Double radius, Integer res) {
        EdgeMatrix spherePoints = new EdgeMatrix();
        spherePoints.addSphere(cx, cy, cz, radius);
        Edge[] points = (Edge[]) spherePoints.edges.toArray(new Edge[0]);
        res += 1;
        res += points.length % res;
        for (int i = 0; i < points.length; i += res) {
            for (int j = 0; j < res - 1; j++) {
                addTriangle(points[(i + j) % points.length].start,
                    points[(i + res + ((j + 1) % res)) % points.length].start,
                    points[(i + j + res) % points.length].start
                    );
                addTriangle(points[(i + j) % points.length].start,
                    points[(i + ((j + 1) % res)) % points.length].start,
                    points[(i + res + ((j + 1) % res)) % points.length].start);
            }
        }
    }
}
