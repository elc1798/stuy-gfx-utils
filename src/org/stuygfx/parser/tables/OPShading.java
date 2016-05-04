package org.stuygfx.parser.tables;

public class OPShading extends OPCode {

    private String name;

    public OPShading(String name) {
        this.name = name;
    }

    public String getType() {
        return name;
    }

    public String toString() {
        return "Shading: " + name;
    }
}
