package org.stuygfx.parser.tables;

public class OPSet extends OPCode {

    private String name;
    private double value;

    public OPSet(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getKnob() {
        return name;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "Set: " + name + " - " + value;
    }
}
