package org.stuygfx.math;

import java.util.Arrays;

public class Matrix {

    public double[][] data;

    public Matrix(int rows, int cols) {
        data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        this.data = data;
    }

    public int rows() {
        return data.length;
    }

    public int cols() {
        return data[0].length;
    }

    public void print() {
        for (int i = 0; i < data.length; i++) {
            System.out.println(Arrays.toString(data[i]));
        }
    }

    public Matrix clone() {
        Matrix clone = new Matrix(this.rows(), this.cols());
        for (int i = 0; i < this.rows(); i++) {
            for (int j = 0; j < this.cols(); j++) {
                clone.data[i][j] = this.data[i][j];
            }
        }
        return clone;
    }
}
