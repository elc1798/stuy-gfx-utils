package org.stuygfx.parser.tables;

public class OPFocal extends OPCode {

    private double value;

    public OPFocal(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "Focal: " + value;
    }

}
