package org.stuygfx.math;

import static org.stuygfx.math.MatrixMath.identityMatrix;

import org.stuygfx.graphics.Edge;
import org.stuygfx.graphics.EdgeMatrix;
import org.stuygfx.graphics.PolygonMatrix;
import org.stuygfx.graphics.Triangle;

public class Transformations {

    public static Matrix getTranslationMatrix(int dx, int dy, int dz) {
        Matrix transMat = identityMatrix(4);
        transMat.data[0][3] = dx;
        transMat.data[1][3] = dy;
        transMat.data[2][3] = dz;
        return transMat;
    }

    public static Matrix getScaleMatrix(double xFac, double yFac, double zFac) {
        Matrix scaleMat = identityMatrix(4);
        scaleMat.data[0][0] = xFac;
        scaleMat.data[1][1] = yFac;
        scaleMat.data[2][2] = zFac;
        return scaleMat;
    }

    public static Matrix getRotXMatrix(double theta) {
        theta = Math.toRadians(theta);
        double[][] data = { { 1, 0, 0, 0 }, { 0, Math.cos(theta), -Math.sin(theta), 0 },
                { 0, Math.sin(theta), Math.cos(theta), 0 }, { 0, 0, 0, 1 } };
        return new Matrix(data);
    }

    public static Matrix getRotYMatrix(double theta) {
        theta = Math.toRadians(theta);
        double[][] data = { { Math.cos(theta), 0, -Math.sin(theta), 0 }, { 0, 1, 0, 0 },
                { Math.sin(theta), 0, Math.cos(theta), 0 }, { 0, 0, 0, 1 } };
        return new Matrix(data);
    }

    public static Matrix getRotZMatrix(double theta) {
        theta = Math.toRadians(theta);
        double[][] data = { { Math.cos(theta), -Math.sin(theta), 0, 0 }, { Math.sin(theta), Math.cos(theta), 0, 0 },
                { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
        return new Matrix(data);
    }

    public static void applyTransformation(Matrix transMatrix, EdgeMatrix em) {
        for (Edge e : em.edges) {
            e.start.fromMatrix(MatrixMath.crossProduct(transMatrix, e.start.toMatrix()));
            e.end.fromMatrix(MatrixMath.crossProduct(transMatrix, e.end.toMatrix()));
        }
    }

    public static void applyTransformation(Matrix transMatrix, PolygonMatrix pm) {
        for (Triangle t : pm.polygons) {
            t.p1.fromMatrix(MatrixMath.crossProduct(transMatrix, t.p1.toMatrix()));
            t.p2.fromMatrix(MatrixMath.crossProduct(transMatrix, t.p2.toMatrix()));
            t.p3.fromMatrix(MatrixMath.crossProduct(transMatrix, t.p3.toMatrix()));
        }
    }
}
