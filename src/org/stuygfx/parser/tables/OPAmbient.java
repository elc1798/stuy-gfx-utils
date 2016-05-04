package org.stuygfx.parser.tables;

public class OPAmbient extends OPCode {

    private double[] rgb;

    public OPAmbient(double[] rgb) {
        this.rgb = rgb;
    }

    public double[] getRgb() {
        return rgb;
    }

    @Override
    public String toString() {
        return "Ambient: RGB - " + rgb[0] + " " + rgb[1] + " " + rgb[2];
    }
}
