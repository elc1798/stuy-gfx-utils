package org.stuygfx.graphics;

import org.stuygfx.math.Matrix;

public class Point {

    public int x;
    public int y;
    public int z;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Matrix m) {
        assert (m.rows() == 4 && m.cols() == 1);
        this.x = (int) m.data[0][0];
        this.y = (int) m.data[1][0];
        this.z = (int) m.data[2][0];
    }

    public Matrix toMatrix() {
        double[][] values = { { this.x }, { this.y }, { this.z }, { 1 } };
        return new Matrix(values);
    }

    public void fromMatrix(Matrix m) {
        assert (m.rows() == 4 && m.cols() == 1);
        this.x = (int) m.data[0][0];
        this.y = (int) m.data[1][0];
        this.z = (int) m.data[2][0];
    }
}
