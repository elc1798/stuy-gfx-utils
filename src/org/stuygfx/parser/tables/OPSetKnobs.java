package org.stuygfx.parser.tables;

public class OPSetKnobs extends OPCode {

    private double value;

    public OPSetKnobs(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "Setknobs: " + value;
    }

}
