package org.stuygfx.graphics;

import static org.stuygfx.CONSTANTS.DEFAULT_VIEW_VEC;

import org.stuygfx.math.Matrix;
import org.stuygfx.math.MatrixMath;

public class Triangle {

    public Point p1;
    public Point p2;
    public Point p3;

    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    /**
     * Implementation of backface culling utilizing the following steps:
     * <br>
     * 1. Calculate the normal vector N
     * <br>
     * 2. Find theta between N and V
     * <br>
     * 3. If 90 <= theta <= 270, return true
     * 
     * @return true if 90 <= theta <= 270
     */
    public boolean shouldDraw() {
        Matrix N = new Matrix(new double[][] {
            {
                (p2.y - p1.y) * (p3.z - p1.z) - (p2.z - p1.z) * (p3.y - p1.y),
                (p2.z - p1.z) * (p3.x - p1.x) - (p2.x - p1.x) * (p3.z - p1.z),
                (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)
            }
        });
        double cosTheta = MatrixMath.dotProduct(N, DEFAULT_VIEW_VEC) / MatrixMath.magnitude(N);
        return cosTheta <= 0.0;
    }
}
