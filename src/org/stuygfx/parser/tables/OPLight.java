package org.stuygfx.parser.tables;

public class OPLight extends OPCode {

    private double[] rgb;
    private double[] location;
    private String name;

    public OPLight(String name, double[] rgb, double[] location) {
        this.name = name;
        this.rgb = rgb;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public double[] getRgb() {
        return rgb;
    }

    public double[] getLocation() {
        return location;
    }

    public String toString() {
        return "Light: RGB - " + triple(rgb) +
            " Location - " + triple(location);
    }

}
