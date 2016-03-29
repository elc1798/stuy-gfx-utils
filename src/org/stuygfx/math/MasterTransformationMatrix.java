package org.stuygfx.math;

public class MasterTransformationMatrix extends Matrix {

    public MasterTransformationMatrix() {
        super(4, 4);
        reset();
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

    public void reset() {
        this.set(MatrixMath.identityMatrix(4));
    }

    public void addTransformation(Matrix trans) {
        this.set(MatrixMath.crossProduct(trans, this));
    }

    public void addRotX(Double theta) {
        this.addTransformation(Transformations.getRotXMatrix(theta));
    }

    public void addRotY(Double theta) {
        this.addTransformation(Transformations.getRotYMatrix(theta));
    }

    public void addRotZ(Double theta) {
        this.addTransformation(Transformations.getRotXMatrix(theta));
    }

    public void addTranslate(Integer dx, Integer dy, Integer dz) {
        this.addTransformation(Transformations.getTranslationMatrix(dx, dy, dz));
    }

    public void addScale(Double xFac, Double yFac, Double zFac) {
        this.addTransformation(Transformations.getScaleMatrix(xFac, yFac, zFac));
    }
}
