package org.stuygfx.graphics.Lighting;

import org.stuygfx.graphics.Pixel;

public class PointSource {
    public Pixel color;
    public double[] location;

    public PointSource(Pixel color, double[] location) {
        this.color = color;
        this.location = location;
    }
}
