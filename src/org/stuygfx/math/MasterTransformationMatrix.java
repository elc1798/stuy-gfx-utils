package org.stuygfx.math;

public class MasterTransformationMatrix extends Matrix {

    public MasterTransformationMatrix() {
        super(4, 4);
    }

    public MasterTransformationMatrix(int rows, int cols) {
        super(rows, cols);
    }

    public MasterTransformationMatrix(double[][] data) {
        super(data);
    }

    public void set(Matrix trans) {
        assert (trans.rows() == 4 && trans.cols() == 4);
        this.data = trans.data;
    }

    public void addTrans(Matrix trans) {
        this.set(MatrixMath.crossProduct(trans.data, this.data));
    }
}
