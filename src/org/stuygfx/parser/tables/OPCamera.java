package org.stuygfx.parser.tables;

public class OPCamera extends OPCode {

    private double[] eye;
    private double[] aim;

    public OPCamera(double[] eye, double[] aim) {
        this.eye = eye;
        this.aim = aim;
    }

    public double[] getEye() {
        return eye;
    }

    public double[] getAim() {
        return aim;
    }

    public String toString() {
        return "Camera: Eye - " + triple(eye) +
            " Aim - " + triple(aim);
    }

}
