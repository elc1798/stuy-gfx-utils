package org.stuygfx.math;

public class MatrixMath {

    public static double dotProduct(Matrix m1, Matrix m2) {
        assert (m1.rows() == 1 && m2.rows() == 1);
        assert (m1.cols() == m2.cols());
        double sum = 0.0;
        for (int i = 0; i < m1.cols(); i++) {
            sum += m1.data[0][i] * m2.data[0][i];
        }
        return sum;
    }

    public static double dotProduct(double[][] m1, double[][] m2) {
        return dotProduct(new Matrix(m1), new Matrix(m2));
    }

    public static Matrix crossProduct(Matrix m1, Matrix m2) {
        assert (m1.cols() == m2.rows());
        Matrix crossProd = new Matrix(m1.rows(), m2.cols());
        for (int i = 0; i < m1.rows(); i++) {
            for (int j = 0; j < m2.cols(); j++) {
                // Construct temporary mats to dot product with
                Matrix tmp1 = new Matrix(1, m1.cols());
                for (int k = 0; k < m1.cols(); k++) {
                    tmp1.data[0][k] = m1.data[i][k];
                }

                Matrix tmp2 = new Matrix(1, m2.rows());
                for (int k = 0; k < m2.rows(); k++) {
                    tmp2.data[0][k] = m2.data[k][j];
                }

                crossProd.data[i][j] = dotProduct(tmp1, tmp2);
            }
        }
        return crossProd;
    }

    public static Matrix crossProduct(double[][] m1, double[][] m2) {
        return crossProduct(new Matrix(m1), new Matrix(m2));
    }

    public static void scalarMultiplyInPlace(Matrix m, int factor) {
        for (int i = 0; i < m.rows(); i++) {
            int j;
            for (j = 0; j < m.cols(); j++) {
                m.data[i][j] *= factor;
            }
        }
    }

    public static Matrix scalarMultiply(Matrix m, int factor) {
        Matrix clone = m.clone();
        scalarMultiplyInPlace(clone, factor);
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
}
