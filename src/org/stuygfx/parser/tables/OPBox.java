package org.stuygfx.parser.tables;

public class OPBox extends OPShape {

    private double[] p1;
    private double[] p2;
    private String cs2;

    public OPBox(double[] p1, String cs, double[] p2, String cs2, String constants) {
        this.p1 = p1;
        this.p2 = p2;
        this.cs = cs;
        this.cs2 = cs2;
        this.constants = constants;
    }

    public String toString() {
        return "Box: " + "p1 - " + triple(p1) +
            "p2 - " + triple(p2) +
            " cs1 - " + cs + " cs2 - " + cs2 + " Contsants - " + constants;
    }

    public double[] getRootCoor() {
        return p1;
    }

    public double[] getDimensions() {
        return p2;
    }

    public String getCs2() {
        return cs2;
    }

}
