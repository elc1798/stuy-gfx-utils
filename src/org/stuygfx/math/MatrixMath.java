package org.stuygfx.math;

public class MatrixMath {

    public static double dotProduct(double[][] m1, double[][] m2) {
        assert (m1.length == 1 && m2.length == 1);
        assert (m1[0].length == m2[0].length);
        double sum = 0.0;
        for (int i = 0; i < m1[0].length; i++) {
            sum += m1[0][i] * m2[0][i];
        }
        return sum;
    }

    public static double dotProduct(Matrix m1, Matrix m2) {
        return dotProduct(m1.data, m2.data);
    }

    public static Matrix crossProduct(double[][] m1, double[][] m2) {
        assert (m1[0].length == m2.length);
        double[][] crossProd = new double[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                double[][] tmp1 = new double[1][m1[0].length];
                for (int k = 0; k < m1[0].length; k++) {
                    tmp1[0][k] = m1[i][k];
                }

                double[][] tmp2 = new double[1][m2.length];
                for (int k = 0; k < m2.length; k++) {
                    tmp2[0][k] = m2[k][j];
                }
                crossProd[i][j] = dotProduct(tmp1, tmp2);
            }
        }
        return new Matrix(crossProd);
    }

    public static Matrix crossProduct(Matrix m1, Matrix m2) {
        return crossProduct(m1.data, m2.data);
    }

    public static Matrix scalarMultiply(Matrix m, double factor) {
        Matrix clone = m.clone();
        for (int i = 0; i < clone.rows(); i++) {
            for (int j = 0; j < clone.cols(); j++) {
                clone.data[i][j] *= factor;
            }
        }
        return clone;
    }

    public static Matrix subtract(Matrix m1, Matrix m2) {
        assert (m1.rows() == m2.rows() && m1.cols() == m2.cols());
        Matrix clone = m1.clone();
        for (int i = 0; i < clone.rows(); i++) {
            for (int j = 0; j < clone.cols(); j++) {
                clone.data[i][j] -= m2.data[i][j];
            }
        }
        return clone;
    }

    public static Matrix identityMatrix(int size) {
        Matrix idMat = new Matrix(size, size);
        for (int i = 0; i < idMat.rows(); i++) {
            for (int j = 0; j < idMat.cols(); j++) {
                idMat.data[i][j] = (i == j) ? 1 : 0;
            }
        }
        return idMat;
    }

    public static double magnitude(Matrix vec) {
        assert (vec.rows() == 1);
        double sum = 0;
        for (double d : vec.data[0]) {
            sum += d * d;
        }
        return Math.sqrt(sum);
    }

    public static Matrix normalize(Matrix vec) {
        assert (vec.rows() == 1);
        Matrix normalized = vec.clone();
        double magnitude = magnitude(vec);
        for (int i = 0; i < normalized.data[0].length; i++) {
            normalized.data[0][i] = normalized.data[0][i] / magnitude;
        }
        return normalized;
    }
}
