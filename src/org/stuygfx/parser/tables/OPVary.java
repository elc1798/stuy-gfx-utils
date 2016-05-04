package org.stuygfx.parser.tables;

public class OPVary extends OPCode {

    private String knob;
    private int startframe, endframe;
    private double startval, endval;

    public OPVary(String knob, int startframe, int endframe,
        double startval, double endval) {
        this.knob = knob;
        this.startframe = startframe;
        this.endframe = endframe;
        this.startval = startval;
        this.endval = endval;
    }

    public int getStartframe() {
        return startframe;
    }

    public int getEndframe() {
        return endframe;
    }

    public double getStartval() {
        return startval;
    }

    public double getEndval() {
        return endval;
    }

    public String getKnob() {
        return knob;
    }

    public String toString() {
        return "Vary: " + knob + " - " + startframe + " to " + endframe +
            " values - " + startval + " to " + endval;
    }

}
