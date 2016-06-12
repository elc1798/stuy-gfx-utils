package org.stuygfx.graphics.Lighting;

import org.stuygfx.graphics.Pixel;
import org.stuygfx.math.Matrix;

public class PointSource {
    public Pixel color;
    public Matrix lightVector;

    public PointSource(Pixel color, double[] location) {
        this.color = color;
        this.lightVector = new Matrix(new double[][] { location });
    }

    public PointSource(double[] color, double[] location) {
        this.color = new Pixel((int) color[0], (int) color[1], (int) color[2]);
        this.lightVector = new Matrix(new double[][] { location });
    }
}
