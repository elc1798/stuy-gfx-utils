package org.stuygfx.graphics;

import org.stuygfx.math.Matrix;

public class Point {

    public double x;
    public double y;
    public double z;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Point(double x, double y, double z) {
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
        double[][] values = {
            {
                this.x
            }, {
                this.y
            }, {
                this.z
            }, {
                1
            }
        };
        return new Matrix(values);
    }

    public void fromMatrix(Matrix m) {
        assert (m.rows() == 4 && m.cols() == 1);
        this.x = (int) m.data[0][0];
        this.y = (int) m.data[1][0];
        this.z = (int) m.data[2][0];
    }

    public Point clone() {
        return new Point(this.x, this.y, this.z);
    }

    public int compareTo(Point pt, char axis) {
        switch (axis) {
            case 'x':
            case 'X':
                return (int) (this.x - pt.x);
            case 'y':
            case 'Y':
                return (int) (this.y - pt.y);
            case 'z':
            case 'Z':
                return (int) (this.z - pt.z);
            default:
                return 0;
        }
    }
}
