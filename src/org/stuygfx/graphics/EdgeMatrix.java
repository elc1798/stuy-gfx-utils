package org.stuygfx.graphics;

import static org.stuygfx.CONSTANTS.CEILING;
import static org.stuygfx.CONSTANTS.STEP;

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

        x0 = xFunc.get(0);
        y0 = yFunc.get(0);

        for (double t = STEP; t < CEILING; t += STEP) {
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

        double[][] xIn = {
            {
                x0, dx0, x1, dx1
            }
        };

        double[][] yIn = {
            {
                y0, dy0, y1, dy1
            }
        };

        double[][] inverse = {
            {
                2, -2, 1, 1
            }, {
                -3, 3, -2, -1
            }, {
                0, 0, 1, 0
            }, {
                1, 0, 0, 0
            }
        };

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

    public void addBox(Integer x, Integer y, Integer z, Integer dx, Integer dy, Integer dz) {
        addEdge(new Point(x, y, z), new Point(x, y, z + dz));
        addEdge(new Point(x, y, z), new Point(x + dx, y, z));
        addEdge(new Point(x, y, z), new Point(x, y + dy, z));
        addEdge(new Point(x, y, z + dz), new Point(x + dx, y, z + dz));
        addEdge(new Point(x + dx, y, z), new Point(x + dx, y, z + dz));
        addEdge(new Point(x + dx, y, z + dz), new Point(x + dx, y + dy, z + dz));
        addEdge(new Point(x, y + dy, z), new Point(x, y + dy, z + dz));
        addEdge(new Point(x, y + dy, z + dz), new Point(x + dx, y + dy, z + dz));
        addEdge(new Point(x, y + dy, z), new Point(x + dx, y + dy, z));
        addEdge(new Point(x + dx, y + dy, z + dz), new Point(x + dx, y + dy, z));
        addEdge(new Point(x, y, z + dz), new Point(x, y + dy, z + dz));
        addEdge(new Point(x + dx, y, z), new Point(x + dx, y + dy, z));
    }

    public void addSphere(Double cx, Double cy, Double cz, Double radius) {
        double x, y, z;

        for (int p = 0; p < 50; p++) {
            double rot = (double) p / 50;
            for (int t = 0; t < 50; t++) {
                double circ = (double) t / 50;
                x = radius * Math.cos(2 * Math.PI * circ) + cx;
                y = radius * Math.sin(2 * Math.PI * circ) * Math.cos(rot * 2 * Math.PI) + cy;
                z = radius * Math.sin(2 * Math.PI * circ) * Math.sin(rot * 2 * Math.PI) + cz;
                addPoint(new Point((int) x, (int) y, (int) z));
            }
        }
    }

    public void addTorus(Double cx, Double cy, Double cz, Double r1, Double r2) {
        double x, y, z;

        for (int p = 0; p < 10; p++) {
            double rot = (double) p / 10;
            for (int t = 0; t < 10; t ++) {
                double circ = (double) t / 10;
                x = Math.cos(rot * 2 * Math.PI) * (r1 * Math.cos(circ * 2 * Math.PI) + r2) + cx;
                y = r1 * Math.sin(circ * 2 * Math.PI) + cy;
                z = Math.sin(rot * 2 * Math.PI) * (r1 * Math.cos(circ * 2 * Math.PI) + r2) + cz;
                addPoint(new Point((int) x, (int) y, (int) z));
            }
        }
    }
}
