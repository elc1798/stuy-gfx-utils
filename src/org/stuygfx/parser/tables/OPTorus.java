package org.stuygfx.parser.tables;

public class OPTorus extends OPShape {

    private double[] center;
    private double R, r;

    public OPTorus(double[] center, double R, double r, String cs, String constants) {
        this.center = center;
        this.r = r;
        this.R = R;
        this.cs = cs;
        this.constants = constants;
    }

    public String toString() {
        return "Torus: " + "Center - " + triple(center) +
            " R - " + R + " r - " + r + " cs - " + cs + " Contsants - " + constants;
    }

    public double[] getCenter() {
        return center;
    }

    public double getOuterRadius() {
        return R;
    }

    public double getCrossSectionRadius() {
        return r;
    }

}
