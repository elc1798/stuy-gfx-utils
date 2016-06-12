package org.stuygfx.graphics.Lighting;

import org.stuygfx.graphics.Pixel;

public class AmbientSource {
    public Pixel color;

    public AmbientSource(Pixel color, double[] location) {
        this.color = color;
    }
}
