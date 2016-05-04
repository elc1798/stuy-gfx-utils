package org.stuygfx.parser.tables;

public class OPMove extends OPTrans {

    private double[] t;

    public OPMove(double[] t, String knob) {
        this.t = t;
        this.knob = knob;
    }

    public String toString() {
        return "Move: " + "t - " + triple(t) +
            " Knob - " + knob;
    }

    public double[] getValues() {
        return t;
    }

    public String getKnob() {
        return knob;
    }

}
