package org.stuygfx.parser.tables;

public class OPRotate extends OPTrans {

    private double deg;
    private char axis;

    public OPRotate(char axis, double deg, String knob) {
        this.axis = axis;
        this.deg = deg;
        this.knob = knob;
    }

    public String toString() {
        return "Rotate: Axis - " + axis + " Deg - " + deg;
    }

    public char getAxis() {
        return axis;
    }

    public double getDeg() {
        return deg;
    }

    public String getKnob() {
        return knob;
    }

}
