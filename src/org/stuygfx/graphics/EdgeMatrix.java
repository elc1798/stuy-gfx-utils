package org.stuygfx.graphics;

import java.util.ArrayList;

import org.stuygfx.math.Matrix;
import org.stuygfx.math.MatrixMath;
import org.stuygfx.math.Parametric;

public class EdgeMatrix {

    public ArrayList<Edge> edges;

    public EdgeMatrix() {
        edges = new ArrayList<Edge>();
    }

    public void empty() {
        edges.clear();
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

    public void addParametric(Parametric xFunc, Parametric yFunc) {
        double x0, y0, x, y;

        double CEILING = 1.0001;
        double step = 0.0025;
        x0 = xFunc.get(0);
        y0 = yFunc.get(0);

        for (double t = step; t < CEILING; t += step) {
            x = xFunc.get(t);
            y = yFunc.get(t);

            addEdge(new Point((int) x0, (int) y0), new Point((int) x, (int) y));
            x0 = x;
            y0 = y;
        }
    }

    public void addCircle(final Point center, final Double radius) {
        Parametric x = new Parametric() {
            @Override
            public Double get(double t) {
                return radius * Math.cos(2 * Math.PI * t) + center.x;
            }
        };

        Parametric y = new Parametric() {
            @Override
            public Double get(double t) {
                return radius * Math.sin(2 * Math.PI * t) + center.y;
            }
        };

        addParametric(x, y);
    }

    public void addHermiteCurve(Double x0, Double y0, Double dx0, Double dy0, Double x1, Double y1, Double dx1,
            Double dy1) {
        double[][] xIn = { { x0, dx0, x1, dx1 } };
        double[][] yIn = { { y0, dy0, y1, dy1 } };

        double[][] inverse = { { 2, -2, 1, 1 }, { -3, 3, -2, -1 }, { 0, 0, 1, 0 }, { 1, 0, 0, 0 } };

        final Matrix xOut = MatrixMath.crossProduct(xIn, inverse);
        final Matrix yOut = MatrixMath.crossProduct(yIn, inverse);

        Parametric x = new Parametric() {
            @Override
            public Double get(double t) {
                return xOut.data[0][0] * Math.pow(t, 3) + xOut.data[0][1] * Math.pow(t, 2) + xOut.data[0][2] * t
                        + xOut.data[0][3];
            }
        };

        Parametric y = new Parametric() {
            @Override
            public Double get(double t) {
                return yOut.data[0][0] * Math.pow(t, 3) + yOut.data[0][1] * Math.pow(t, 2) + yOut.data[0][2] * t
                        + yOut.data[0][3];
            }
        };

        addParametric(x, y);
    }

    public void addBezierCurve(final Double x0, final Double y0, final Double x1, final Double y1, final Double x2,
            final Double y2, final Double x3, final Double y3) {
        Parametric x = new Parametric() {
            @Override
            public Double get(double t) {
                return x0 * Math.pow(1 - t, 3) + 3 * x1 * Math.pow(1 - t, 2) * t + 3 * x2 * (1 - t) * Math.pow(t, 2)
                        + x3 * Math.pow(t, 3);
            }
        };

        Parametric y = new Parametric() {
            @Override
            public Double get(double t) {
                return y0 * Math.pow(1 - t, 3) + 3 * y1 * Math.pow(1 - t, 2) * t + 3 * y2 * (1 - t) * Math.pow(t, 2)
                        + y3 * Math.pow(t, 3);
            }
        };

        addParametric(x, y);
    }
}
