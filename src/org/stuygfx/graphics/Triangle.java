package org.stuygfx.graphics;

import static org.stuygfx.CONSTANTS.DEFAULT_VIEW_VEC;

import org.stuygfx.math.Matrix;
import org.stuygfx.math.MatrixMath;

public class Triangle {

    private Point p1;
    private Point p2;
    private Point p3;

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
    public boolean isFacingOutwards() {
        Matrix A = new Matrix(new double[][] {
            {
                p2.x - p1.x, p2.y - p1.y, p2.z - p1.z
            }
        });
        Matrix B = new Matrix(new double[][] {
            {
                p3.x - p1.x, p3.y - p1.y, p3.z - p1.z
            }
        });
        Matrix N = MatrixMath.crossProduct(A, B);
        double cosTheta = MatrixMath.dotProduct(N, DEFAULT_VIEW_VEC) / MatrixMath.magnitude(N);
        return cosTheta <= 0.0;
    }

}
