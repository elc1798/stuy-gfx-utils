package org.stuygfx.graphics;

import java.util.ArrayList;

public class PolygonMatrix {

    public ArrayList<Triangle> polygons;

    public PolygonMatrix() {
        polygons = new ArrayList<Triangle>();
    }

    public void empty() {
        polygons.clear();
    }

    public void addTriangle(Triangle t) {
        polygons.add(t.clone());
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

    public void addSphere(Double cx, Double cy, Double cz, Double radius) {
        EdgeMatrix spherePoints = new EdgeMatrix();
        spherePoints.addSphere(cx, cy, cz, radius);
        Edge[] points = (Edge[]) spherePoints.edges.toArray(new Edge[0]);
        int n = (int) Math.sqrt(points.length);
        for (int circ = 0; circ < n - 1; circ++) {
            for (int point = 0; point < n / 2; point++) {
                // Points: We make a triangle of P1, P(n + 2), P2 and
                // another of P(n + 1), P(n + 2), P1
                addTriangle(points[point + circ * n].start.clone(),
                    points[point + 1 + (circ + 1) * n].start.clone(),
                    points[point + 1 + circ * n].start.clone());
                addTriangle(points[point + (circ + 1) * n].start.clone(),
                    points[point + 1 + (circ + 1) * n].start.clone(),
                    points[point + circ * n].start.clone());
                // Edge case
                addTriangle(points[point + (n - 1) * n].start.clone(),
                    points[point + 1].start.clone(),
                    points[point + 1 + (n - 1) * n].start.clone());
                addTriangle(points[point].start.clone(),
                    points[point + 1].start.clone(),
                    points[point + (n - 1) * n].start.clone());
            }
        }
    }

    public void addTorus(Double cx, Double cy, Double cz, Double r1, Double r2) {
        EdgeMatrix torusPoints = new EdgeMatrix();
        torusPoints.addTorus(cx, cy, cz, r1, r2);
        Edge[] points = (Edge[]) torusPoints.edges.toArray(new Edge[0]);
        int n = (int) Math.sqrt(points.length);
        for (int circ = 0; circ < n - 1; circ++) {
            for (int point = 0; point < n - 1; point++) {
                // Points: We make a triangle of P1, P(n + 2), P2 and
                // another of P(n + 1), P(n + 2), P1
                addTriangle(points[point + circ * n].start.clone(),
                    points[point + 1 + (circ + 1) * n].start.clone(),
                    points[point + 1 + circ * n].start.clone());
                addTriangle(points[point + (circ + 1) * n].start.clone(),
                    points[point + 1 + (circ + 1) * n].start.clone(),
                    points[point + circ * n].start.clone());
                // Edge case
                addTriangle(points[point + (n - 1) * n].start.clone(),
                    points[point + 1].start.clone(),
                    points[point + 1 + (n - 1) * n].start.clone());
                addTriangle(points[point].start.clone(),
                    points[point + 1].start.clone(),
                    points[point + (n - 1) * n].start.clone());
            }
            // hardcode edge case :D
            addTriangle(points[n - 1 + circ * n].start.clone(),
                points[(circ + 1) * n].start.clone(),
                points[circ * n].start.clone());
            addTriangle(points[n - 1 + (circ + 1) * n].start.clone(),
                points[(circ + 1) * n].start.clone(),
                points[n - 1 + circ * n].start.clone());
            addTriangle(points[n - 1 + (n - 1) * n].start.clone(),
                points[0].start.clone(),
                points[(n - 1) * n].start.clone());
            addTriangle(points[n - 1].start.clone(),
                points[0].start.clone(),
                points[n - 1 + (n - 1) * n].start.clone());
        }
    }

    public PolygonMatrix clone() {
        PolygonMatrix clone = new PolygonMatrix();
        for (Triangle t : polygons) {
            clone.addTriangle(t);
        }
        return clone;
    }
}
